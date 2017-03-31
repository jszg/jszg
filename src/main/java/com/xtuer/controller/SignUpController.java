package com.xtuer.controller;

import com.alibaba.fastjson.TypeReference;
import com.sun.javafx.geom.transform.BaseTransform;
import com.xtuer.bean.EnrollStep4Form;
import com.xtuer.bean.Result;
import com.xtuer.bean.UserPortalLog;
import com.xtuer.constant.RedisKey;
import com.xtuer.constant.SignUpConstants;
import com.xtuer.constant.UriView;
import com.xtuer.dto.*;
import com.xtuer.mapper.*;
import com.xtuer.util.BrowserUtils;
import com.xtuer.util.RedisUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class SignUpController {
    private static final int[] DICT_TYPES = {5, 7, 25, 22, 21, 2, 3, 9, 24, 6, 4, 10, 23, 30};
    private static final String[] DICT_TYPENAMES = {"nation", "eduLevel", "schoolQuale", "workUnitType", "learnType",
            "normalMajor", "political", "pthLevel", "postQuale", "degree", "occupation", "idType", "teachGrade", "schoolType"};

    // 1. 创建 logger 对象
    private static Logger logger = LoggerFactory.getLogger(SignUpController.class.getName());

    // 所有资格种类
    @GetMapping(UriView.REST_CERT_TYPE)
    @ResponseBody
    public Result<List<CertType>> getCertTypes() {
        List<CertType> types = redisUtils.get(new TypeReference<List<CertType>>(){}, RedisKey.CERT_TYPES, () -> certTypeMapper.findAll());
        return Result.ok(types);
    }

    // 所有的省
    @GetMapping(UriView.REST_PROVINCES)
    @ResponseBody
    public Result<List<Province>> getProvinces() {
        List<Province> provinces = redisUtils.get(new TypeReference<List<Province>>(){}, RedisKey.PROVINCES, () -> provinceMapper.findAll());
        return Result.ok(provinces);
    }

    // 省下面所有的市
    @GetMapping(UriView.REST_CITIES_BY_PROVINCE)
    @ResponseBody
    public Result<List<City>> getCities(@PathVariable("provinceId") Integer id) {
        String key = String.format(RedisKey.CITIES, id);
        List<City> cities = redisUtils.get(new TypeReference<List<City>>(){}, key, () -> cityMapper.findByParentId(id));
        return Result.ok(cities);
    }

    // 认定机构: 市或省下面的所有的认定机构
    @GetMapping(UriView.REST_ORGS_REQUEST_BY_CERT_TYPE_PROVINCE_CITY)
    @ResponseBody
    public Result<List<Organization>> getOrgByCity(@PathVariable("certTypeId") int certTypeId,@PathVariable("adminLevel") int adminLevel,
                                                   @PathVariable("provinceId") int provinceId, @PathVariable("cityId") int cityId) {
        List<Organization> orgs = Collections.emptyList();
        String key = String.format(RedisKey.REST_ORGS_REQUEST_BY_CERT_TYPE_PROVINCE_CITY, certTypeId,adminLevel, provinceId, cityId);
        orgs = redisUtils.get(new TypeReference<List<Organization>>() {}, key,
                () -> organizationMapper.findByProvinceAndCityAndCertTypeId(certTypeId,adminLevel,provinceId, cityId));
        return Result.ok(orgs);
    }

    // 证书上的机构 和选择的证书签发日期有关(CertTypeOrgTreeController)
    // 父机构
    @GetMapping(UriView.REST_ORGS_BY_ORGTYPE)
    @ResponseBody
    public Result<List<Organization>> getOrgByOrgType(@PathVariable("orgType") int orgType, @RequestParam("date") String certAssignDate) throws ParseException {
        String key = String.format(RedisKey.ORGS_BY_ORGTYPE, orgType);

        Date date = DateUtils.parseDate(certAssignDate, "yyyy-MM-dd");
        List<Organization> organizations = null;
        if (orgType == 4) {
            organizations = redisUtils.get(new TypeReference<List<Organization>>(){}, key, () -> organizationMapper.findByOrgTypeEq4());
        } else {
            organizations = redisUtils.get(new TypeReference<List<Organization>>() {}, key, () -> organizationMapper.findByOrgType(orgType));
        }
        organizations = this.recoverOrginalOrgName(0,date,organizations);
        return Result.ok(organizations);
    }

    // 子机构
    @GetMapping(UriView.REST_ORGS_BY_PARENT)
    @ResponseBody
    public Result<List<Organization>> getOrgByParentId(@PathVariable("parentId") int parentId, @RequestParam("date") String certAssignDate) throws ParseException {
        String key = String.format(RedisKey.ORGS_BY_PARENT, parentId);
        Date date = DateUtils.parseDate(certAssignDate, "yyyy-MM-dd");
        List<Organization> list = redisUtils.get(new TypeReference<List<Organization>>(){}, key, () -> organizationMapper.findByParentId(parentId));
        list = this.recoverOrginalOrgName(parentId,date,list);
        return Result.ok(list);
    }

    //证书上的认定机构过滤
    private List<Organization> recoverOrginalOrgName(Integer parentID, Date certAssign, List<Organization> orgs) {
        List<Organization> all = new ArrayList<Organization>();
        if (orgs != null && orgs.size() > 0) {
            all.addAll(orgs);
        }
        if(certAssign != null){
            if(parentID == 0){
                List<OrgNameLog> orgNameLogList = commonMapper.findOrgNameLog();
                orgNameLogList.stream().filter(onl -> onl.getChangeDate() != null && onl.getChangeDate().after(certAssign)).forEach(onl -> {
                    this.renameOrAddOrg(all, onl.getOrgId(), onl.getOldName());
                });
            }else{
                //更名后的机构，找变更之前的parent，变更时间，在，证书签发日期之后，证书签发时有效，不存在刚添加进去。存在则名称为变更前的名称。
                List<OrgNameLog> orgNameLogs = commonMapper.findByOldParentId(parentID);
                orgNameLogs.stream().filter(onl -> onl.getChangeDate() != null && onl.getChangeDate().after(certAssign)).forEach(onl -> {
                    this.renameOrAddOrg(all, onl.getOrgId(), onl.getOldName());
                });
                //更名后的机构，变更到parent之中了，变更时间，在，证书签发日期之后，失效的了，删除
                List<OrgNameLog> orgNameLogList = commonMapper.findOrgByNewDiffOrg(parentID);
                orgNameLogList.stream().filter(onl -> onl.getChangeDate() != null && onl.getChangeDate().after(certAssign)).forEach(onl -> {
                    all.remove(new Organization(onl.getOrgId()));
                });
                //撤销或合并机构，撤销或合并时间，在，证书签发日期，之前，说明机构失效了，删除
                List<OrgAnnulLog> orgAnnulLogs = commonMapper.findOrgAnnulLogByParentId(parentID);
                orgAnnulLogs.stream().filter(oal -> oal.getAnnulDate() != null && oal.getAnnulDate().before(certAssign)).forEach(oal -> {
                    all.remove(new Organization(oal.getOrgId()));
                });
            }
        }
        return all;
    }

    private void renameOrAddOrg(Collection<Organization> orgs, Integer orgId, String reName) {
        boolean find = false;
        for (Organization org : orgs) {
            if (org.getId().equals(orgId)) {
                find = true;
                org.setName(reName);
                break;
            }
        }
        if (!find) {
            Organization org = new Organization(orgId);
            org.setName(reName);
            orgs.add(org);
        }
    }

    // 注册机构
    @GetMapping(UriView.REST_ORGS_REG)
    @ResponseBody
    public Result<List<Organization>> getRegOrgs(@RequestParam("teachGrade") int teachGrade,
                @RequestParam("cityId") int cityId,
                @RequestParam(value = "provinceCity", required = false, defaultValue = "false") boolean provinceCity) {
        List<Organization> organizations = Collections.emptyList();

        // teachGrade查询code是否==7
        TypeReference<List<CertType>> certTypeReference = new TypeReference<List<CertType>>() {};
        List<CertType> certTypes = redisUtils.get(certTypeReference, String.format(RedisKey.CERTTYPE_BY_TEACHGRADE, teachGrade),
                () -> certTypeMapper.findByTeachGrade(teachGrade));
        if (certTypes.isEmpty()) return Result.error(organizations);

        String key = String.format(RedisKey.ORGS_ZHUCE, teachGrade, cityId);
        TypeReference<List<Organization>> orgReference = new TypeReference<List<Organization>>() {};

        if (has7(certTypes)) {
            organizations = redisUtils.get(orgReference, key, () -> organizationMapper.findByOrgId(cityId));
        } else {
            // 直辖市
            if (provinceCity) {
                organizations = redisUtils.get(orgReference, key, () -> organizationMapper.findByProvinceCity(cityId));
            } else {
                organizations = redisUtils.get(orgReference, key, () -> organizationMapper.findByCity(cityId));
            }
        }

        return Result.ok(organizations);
    }

    // 省下面的第一级任教学科
    @GetMapping(UriView.REST_SUBJECTS_ROOT)
    @ResponseBody
    public Result<List<Subject>> getRootSubjects(@PathVariable("provinceId") int provinceId, @PathVariable("certTypeId") int certTypeId) {
        String key = String.format(RedisKey.SUBJECTS_ROOT, provinceId, certTypeId);
        List<Subject> subjects = redisUtils.get(new TypeReference<List<Subject>>(){}, key, () -> subjectMapper.findRoots(provinceId, certTypeId));

        return Result.ok(subjects);
    }

    // 省下面指定父节点的任教学科
    @GetMapping(UriView.REST_SUBJECTS_CHILDREN)
    @ResponseBody
    public Result<List<Subject>> getChildrenSubjects(@PathVariable("provinceId") int provinceId, @PathVariable("parentId") int parentId) {
        String key = String.format(RedisKey.SUBJECTS_CHILDREN, provinceId, parentId);
        List<Subject> subjects = redisUtils.get(new TypeReference<List<Subject>>(){}, key,
                () -> subjectMapper.findByParentAndProvince(parentId, provinceId));

        return Result.ok(subjects);
    }

    // 非统考第三步任教学科父节点
    @GetMapping(UriView.REST_REQUEST_SUBJECTS)
    @ResponseBody
    public Result<List<Subject>> getRequestRootSubjects(@PathVariable("provinceId") int provinceId, @PathVariable("teachGrade") int teachGrade) {
        String key = String.format(RedisKey.REST_REQUEST_SUBJECTS, provinceId, teachGrade);
        List<Subject> subjects = redisUtils.get(new TypeReference<List<Subject>>(){}, key, () -> subjectMapper.findByCertTypeAndProvince(provinceId, teachGrade));
        return Result.ok(subjects);
    }

    // 非统考第三步任教学科子节点
    @GetMapping(UriView.REST_REQUEST_SUBJECTS_CHILDREN)
    @ResponseBody
    public Result<List<Subject>> getRequestChildrenSubjects(@PathVariable("provinceId") int provinceId, @PathVariable("parentId") int parentId) {
        String key = String.format(RedisKey.REST_REQUEST_SUBJECTS_CHILDREN, provinceId, parentId);
        List<Subject> subjects = redisUtils.get(new TypeReference<List<Subject>>(){}, key,
                () -> subjectMapper.findChildByCertTypeAndProvince(provinceId,parentId));
        return Result.ok(subjects);
    }


    // 证书上的任教学科 (OrgCertTypeSubjectTreeController)
    // 父节点
    @GetMapping(UriView.REST_SUBJECTS_BY_CERT_TYPE)
    @ResponseBody
    public Result<List<Subject>> getSubjectByCertType(@PathVariable("certTypeId") int certTypeId) {
        String key = String.format(RedisKey.SUBJECTS_BY_CERTTYPE, certTypeId);
        List<Subject> list = redisUtils.get(new TypeReference<List<Subject>>(){}, key, () -> subjectMapper.findByCertType(certTypeId));
        return Result.ok(list);
    }

    // 现任教学科按名称搜索
    @GetMapping(UriView.REST_TEACH_SUBJECT_BY_NAME)
    @ResponseBody
    public Result<List<Subject>> getSubjectByName(@PathVariable("teachGradeId") int teachGradeId,@PathVariable("provinceId") int provinceId, @PathVariable("name") String name) {
        if(name.isEmpty()){
            return Result.ok(null);
        }
        try {
            name = java.net.URLDecoder.decode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<Subject> list = subjectMapper.findByName(teachGradeId,provinceId,name);
        if(list.isEmpty()){
            return Result.ok(null);
        }
        return Result.ok(list);
    }

    // 非统考第三步任教学科按名称查询
    @GetMapping(UriView.REST_REQUEST_SUBJECT_BY_NAME)
    @ResponseBody
    public Result<List<Subject>> getRequestSubjectByName(@PathVariable("teachGrade") int teachGrade,@PathVariable("provinceId") int provinceId, @PathVariable("name") String name) {
        if(name.isEmpty()){
            return Result.ok(null);
        }
        try {
            name = java.net.URLDecoder.decode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<Subject> list = subjectMapper.findRequestSubjectByName(teachGrade,provinceId,name);
        if(list.isEmpty()){
            return Result.ok(null);
        }
        return Result.ok(list);
    }

    // 子节点
    @GetMapping(UriView.REST_SUBJECTS_BY_PARENT)
    @ResponseBody
    public Result<List<Subject>> getSubjectByParent(@PathVariable("parentId") int parentId) {
        String key = String.format(RedisKey.SUBJECTS_BY_PARENT, parentId);
        List<Subject> subjects = redisUtils.get(new TypeReference<List<Subject>>(){}, key, () -> subjectMapper.findByParent(parentId));
        return Result.ok(subjects);
    }

    // 现任教学科 父节点, 子节点@see getChildrenSubjects
    @GetMapping(UriView.REST_SUBJECTS_TEASUBJECT)
    @ResponseBody
    public Result<List<Subject>> getTeaSubjects(@PathVariable("provinceId") int provinceId, @PathVariable("teachGrade") int teachGrade) {
        String key = String.format(RedisKey.SUBJECTS_TEASUBJECT, teachGrade, provinceId);
        List<Subject> list = redisUtils.get(new TypeReference<List<Subject>>(){}, key,
                () -> subjectMapper.findBySubjectTypeAndProvince(teachGrade, provinceId));
        return Result.ok(list);
    }

    // 按照字典类型查询字典数据
    @GetMapping(UriView.REST_DICTS_BY_DICTTYPE)
    @ResponseBody
    public Result<List<Dict>> getDicts(@PathVariable("dictTypeId") int dictTypeId) {
        String key = String.format(RedisKey.DICTS_BY_TYPE, dictTypeId);
        List<Dict> dicts = redisUtils.get(new TypeReference<List<Dict>>(){}, key, () -> dictMapper.findByDictType(dictTypeId));
        return Result.ok(dicts);
    }

    // 所有字典，按类型分类
    @GetMapping(UriView.REST_DICTS)
    @ResponseBody
    public Result<Map<String, List<?>>> getDicts() {
        String key = RedisKey.DICTS;
        Map<String, List<?>> dicts  = redisUtils.get(Map.class, key, () -> {
            Map<String, List<?>> map = new HashMap<>();

            for (int i = 0; i < DICT_TYPES.length; i++) {
                final int type = DICT_TYPES[i];

                if(type == 23) {
                    map.put(DICT_TYPENAMES[i], dictMapper.findTeaGradesByStatus());
                } else if(type == 30) {
                    map.put(DICT_TYPENAMES[i], dictMapper.findByDictTypeStatus(type));
                } else {
                    map.put(DICT_TYPENAMES[i], dictMapper.findByDictType(type));
                }
            }

            map.put(RedisKey.PROVINCES, provinceMapper.findAll());
            map.put(RedisKey.CERT_TYPES, certTypeMapper.findAll());
            return map;
        });
        return Result.ok(dicts);
    }

    // 确认点
    @GetMapping(UriView.REST_LOCAL_SETS)
    @ResponseBody
    public Result<List<LocalSet>> getLocalSets(@PathVariable int orgId,@PathVariable int type) {
        String key = String.format(RedisKey.LOCAL_SETS, orgId, type);
        List<LocalSet> list = redisUtils.get(new TypeReference<List<LocalSet>>() {}, key, () -> localSetMapper.findByOrgId(orgId,type));

        // 过滤时间
        Date now = new Date();
        List<LocalSet> localSets = new ArrayList<>(list.size());
        localSets.addAll(list.stream()
                .filter(l -> now.before(l.getEndDate()) && now.after(l.getValidBeginDate()) && now.before(l.getValidEndDate()))
                .collect(Collectors.toList()));

        return Result.ok(localSets);
    }

    // 确认点信息
    @GetMapping(UriView.REST_LOCAL_SETS_INFO)
    @ResponseBody
    public Result<LocalSetInfo> getLocalSetInfo(@PathVariable int localSetId) {
        LocalSetInfo info = localSetMapper.findById(localSetId);
        return Result.ok(info);
    }

    // 民族
    @GetMapping(UriView.REST_NATIONS)
    @ResponseBody
    public Result<List<Dict>> getNations() {
        return getDicts(5);
    }

    // 现任教学段
    @GetMapping(UriView.REST_TEAGRADES)
    @ResponseBody
    public Result<List<TeachGrade>> getTeaGrades() {
        String key = RedisKey.TEAGRADES;
        List<TeachGrade> list = redisUtils.get(new TypeReference<List<TeachGrade>>(){}, key, () -> dictMapper.findTeaGrades());
        return Result.ok(list);
    }

    // 最高学历:认定报名中,最高学历eduLevels是和资格种类关联的
    @GetMapping(UriView.REST_EDU_LEVELS)
    @ResponseBody
    public Result<List<Dict>> getEduLevels(@PathVariable("certTypeId") int certTypeId) {
        String key = String.format(RedisKey.EDULEVELS, certTypeId);
        List<Dict> eduLevels = redisUtils.get(new TypeReference<List<Dict>>(){}, key, () -> dictMapper.findEduLevels(certTypeId));
        return Result.ok(eduLevels);
    }

    // 所有学校
    @GetMapping(UriView.REST_COLLEGES)
    @ResponseBody
    public Result<List<College>> getColleges() {
        String key = RedisKey.COLLEGES;
        List<College> colleges = redisUtils.get(new TypeReference<List<College>>(){}, key, () -> collegeMapper.findAll());
        return Result.ok(colleges);
    }

    // 所有学校
    @GetMapping(UriView.REST_COLLEGE_BY_NAME)
    @ResponseBody
    public Result<List<College>> getCollegesByName(@PathVariable("name") String name) {
        if (name.isEmpty()){
            return Result.ok(null);
        }
        try {
            name = java.net.URLDecoder.decode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<College> colleges = collegeMapper.findByName(name);
        return Result.ok(colleges);
    }


    // 按省份找学校
    @GetMapping(UriView.REST_COLLEGES_BY_PROVINCE)
    @ResponseBody
    public Result<List<College>> getCollegesByProvinceId(@PathVariable("provinceId") int provinceId) {
        String key = String.format(RedisKey.COLLEGES_BY_PROVINCE, provinceId);
        List<College> colleges = redisUtils.get(new TypeReference<List<College>>(){}, key, () -> collegeMapper.findByProvinceId(provinceId));
        return Result.ok(colleges);
    }

    // 认定的根节点
    @GetMapping(UriView.REST_ZHUCE_MAJOR_PARENT)
    @ResponseBody
    public Result<List<Major>> getRendingRootMajors() {
        String key = RedisKey.MAJORS_ZHUCE_ROOT;
        List<Major> majors = redisUtils.get(new TypeReference<List<Major>>(){}, key, () -> majorMapper.findRoot());
        return Result.ok(majors);
    }

    // 注册的根节点
    @GetMapping(UriView.REST_RENDING_MAJOR_PARENT)
    @ResponseBody
    public Result<List<Major>> getZhuceRootMajors(@PathVariable("certTypeId") int certTypeId, @PathVariable("eduLevelId") int eduLevelId) {
        String key = String.format(RedisKey.MAJORS_RENDING_ROOT, certTypeId, eduLevelId);
        List<Major> majors = redisUtils.get(new TypeReference<List<Major>>(){}, key,
                () -> majorMapper.findByCertTypeIdAndEduLevelId(certTypeId, eduLevelId));
        return Result.ok(majors);
    }

    // 注册或认定的子节点
    @GetMapping(UriView.REST_MAJOR_CHILDREN)
    @ResponseBody
    public Result<List<Major>> getZhuceChildrenMajors(@PathVariable("parentId") int parentId) {
        String key = String.format(RedisKey.MAJORS_ZHUCE_CHILREN, parentId);
        List<Major> majors = redisUtils.get(new TypeReference<List<Major>>(){}, key, () -> majorMapper.findByParentId(parentId));
        return Result.ok(majors);
    }

    //注册第七步最高学历所学专业按名称搜索
    @GetMapping(UriView.REST_MAJOR_SEARCH_BY_NAME)
    @ResponseBody
    public Result<List<Major>> getMajorByName(@PathVariable("name") String name) {
        if(name.isEmpty()){
            return Result.ok(null);
        }
        try {
            name = java.net.URLDecoder.decode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<Major> majors = majorMapper.findByName(name);
        if (majors.isEmpty()) {
            return Result.ok(null);
        }
        return Result.ok(majors);
    }

    //注册第七步最高学历所学专业按名称搜索
    @GetMapping(UriView.REST_MAJOR_SEARCH_BY_NAME_REQUEST)
    @ResponseBody
    public Result<List<Major>> getRequestMajorByName(@PathVariable("name") String name, @PathVariable("certTypeId") int certTypeId, @PathVariable("eduLevelId") int eduLevelId) {
        if(name.isEmpty()){
            return Result.ok(null);
        }
        try {
            name = java.net.URLDecoder.decode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<Major> list = majorMapper.findByCertTypeIdAndEduLevelId(certTypeId,eduLevelId);
        if(!list.isEmpty()){
            List<Major> majorList = majorMapper.findByCertTypeIdAndEduLevelIdAndName(name,certTypeId,eduLevelId);
            return Result.ok(majorList);
        }
        List<Major> majors = majorMapper.findByName(name);
        if (majors.isEmpty()) {
            return Result.ok(null);
        }
        return Result.ok(majors);
    }

    // 认定报名根据资格种类和最高学历选择最高学位
    @GetMapping(UriView.REST_DEGREE_BY_CERT_TYPE_AND_EDU_LEVEL)
    @ResponseBody
    public Result<List<Dict>> getDegreesByCertTypeIdAndEduLevelId(@PathVariable("certTypeId") int certTypeId,@PathVariable("eduLevelId") int eduLevelId) {
        String key = String.format(RedisKey.DEGREES_BY_CERT_TYPE_EDU_LEVEL, certTypeId, eduLevelId);
        List<Dict> degrees = redisUtils.get(new TypeReference<List<Dict>>(){}, key, () -> dictMapper.findDegreessByCertTypeIdAndEduLevelId(certTypeId,eduLevelId));
        if(!degrees.isEmpty()){
            return Result.ok(degrees);
        }else{
            key = String.format(RedisKey.DEGREES_BY_STATUS_TYPE, SignUpConstants.DICT_STATUS_ENABLE, SignUpConstants.ID_DEGREE);
            degrees = redisUtils.get(new TypeReference<List<Dict>>(){}, key, () ->dictMapper.findByDictTypeStatus(SignUpConstants.ID_DEGREE));
            return Result.ok(degrees);
        }
    }

    // 非统考第七步的所学专业root
    @GetMapping(UriView.REST_REQUEST_MAJOR_PARENT)
    @ResponseBody
    public Result<List<Major>> getRequestRootMajors(@PathVariable("provinceId") int provinceId,@PathVariable("certTypeId") int certTypeId,@PathVariable("eduLevelId") int eduLevelId) {
        String key = String.format(RedisKey.MAJORS_REQUEST_ROOT_CERTTYPE_EDULEVEL, certTypeId,eduLevelId);
        //首先查看所学专业和最高学历关联表中是否有数据
        List<Major> majors = redisUtils.get(new TypeReference<List<Major>>(){}, key, () -> majorMapper.findByCertTypeIdAndEduLevelId(certTypeId,eduLevelId));
        if(!majors.isEmpty()){
            return Result.ok(majors);
        }else{
            key = String.format(RedisKey.MAJORS_REQUEST_ROOT, provinceId);
            majors = redisUtils.get(new TypeReference<List<Major>>(){}, key, () -> majorMapper.findRequestMajorRoot(provinceId));
            return Result.ok(majors);
        }
    }

    // 非统考第七步的所学专业root
    @GetMapping(UriView.REST_REQUEST_MAJOR_CHILDREN)
    @ResponseBody
    public Result<List<Major>> getRequestChildMajors(@PathVariable("provinceId") int provinceId,@PathVariable("parentId") int parentId) {
        String key = String.format(RedisKey.MAJORS_REQUEST_CHILREN, provinceId,parentId);
        List<Major> majors = redisUtils.get(new TypeReference<List<Major>>(){}, key, () -> majorMapper.findRequestMajorChildren(provinceId,parentId));
        return Result.ok(majors);
    }

    // 非统考第七步的所学专业root
    @GetMapping(UriView.REST_REQUEST_MAJOR_NAME)
    @ResponseBody
    public Result<List<Major>> getRequestMajorsByName(@PathVariable("provinceId") int provinceId,@PathVariable("name") String name) {
        if(name.isEmpty()){
            return Result.ok(null);
        }
        try {
            name = java.net.URLDecoder.decode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<Major> majors = majorMapper.findRequestMajorByName(provinceId,name);
        if (majors.isEmpty()) {
            return Result.ok(null);
        }
        return Result.ok(majors);
    }


    // 专业技术职务根节点
    @GetMapping(UriView.REST_TECHNICAL_JOB_ROOT)
    @ResponseBody
    public Result<List<TechnicalJob>> getRootTechnicalJobs() {
        String key = RedisKey.TECHNICALJOBS;
        List<TechnicalJob> jobs = redisUtils.get(new TypeReference<List<TechnicalJob>>(){}, key, () -> technicalJobMapper.findRoots());
        return Result.ok(jobs);
    }

    // 专业技术职务子节点
    @GetMapping(UriView.REST_TECHNICAL_JOB_CHILDREN)
    @ResponseBody
    public Result<List<TechnicalJob>> getChildrenTechnicalJobs(@PathVariable("parentId") int parentId) {
        String key = String.format(RedisKey.TECHNICAL_JOB_CHILDREN, parentId);
        List<TechnicalJob> jobs = redisUtils.get(new TypeReference<List<TechnicalJob>>(){}, key, () -> technicalJobMapper.findByParent(parentId));
        return Result.ok(jobs);
    }

    // 专业技术职务子节点
    @GetMapping(UriView.REST_TECHNICAL_JOB_BY_NAME)
    @ResponseBody
    public Result<List<TechnicalJob>> getTechnicalJobsByName(@PathVariable("name") String name){
        if (name.isEmpty()){
            return Result.ok(null);
        }
        try {
            name = java.net.URLDecoder.decode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<TechnicalJob> jobs = technicalJobMapper.findByName(name);
        if(jobs.isEmpty()){
            return Result.ok(null);
        }
        return Result.ok(jobs);
    }

    // 现任教学段 ct_code 是否为7
    private boolean has7(List<CertType> certTypes) {
        boolean has7 = false;
        for (CertType c : certTypes) {
            if (c.getCode() == 7) {
                has7 = true;
                break;
            }
        }
        return has7;
    }

    // 限制库。不缓存，直接查DB
    @GetMapping(UriView.REST_LIMITATION)
    @ResponseBody
    public Result<Limitation> findLimitations(@RequestParam(value = "idno", defaultValue = "", required = false) String idno,
                                              @RequestParam(value = "certno", defaultValue = "", required = false) String certno) {
        List<Limitation> limitations = commonMapper.findLimitation(idno, certno);
        if (limitations.isEmpty()) {
            return Result.ok(null);
        }

        return Result.ok(limitations.get(0));
    }

    // 注册历史表。不缓存，直接查DB
    @GetMapping(UriView.REST_ENROLLHISTORY)
    @ResponseBody
    public Result<EnrollHistory> findEnrollhistory(@RequestParam(value = "idno", defaultValue = "", required = false) String idno,
                                                   @RequestParam(value = "certno", defaultValue = "", required = false) String certno) {
        List<EnrollHistory> list = commonMapper.findEnrollHistory(idno, certno);
        if (list.isEmpty()) {
            return Result.ok(null);
        }
        return Result.ok(list.get(0));
    }

    // 认定历史库。不缓存，直接查DB
    @GetMapping(UriView.REST_HISTORYVALID)
    @ResponseBody
    public Result<HistoryValid> findHistoryValid(@RequestParam(value = "idno", defaultValue = "", required = false) String idno,
                                                 @RequestParam(value = "certno", defaultValue = "", required = false) String certno) {
        List<HistoryValid> list = commonMapper.findHistoryValid(idno, certno);
        if (list.isEmpty()) {
            return Result.ok(null);
        }
        return Result.ok(list.get(0));
    }

    // 认定正式表。不缓存，直接查DB
    @GetMapping(UriView.REST_REGISTRATION)
    @ResponseBody
    public Result<Registration> findRegistration(@RequestParam(value = "idno", defaultValue = "", required = false) String idno,
                                                 @RequestParam(value = "certno", defaultValue = "", required = false) String certno) {
        List<Registration> list = commonMapper.findRegistration(idno, certno);
        if (list.isEmpty()) {
            return Result.ok(null);
        }
        return Result.ok(list.get(0));
    }

    // 注册正式表。不缓存，直接查DB
    @GetMapping(UriView.REST_ENROLLMENT)
    @ResponseBody
    public Result<Enrollment> findEnrollment(@RequestParam(value = "idno", defaultValue = "", required = false) String idno,
                                             @RequestParam(value = "certno", defaultValue = "", required = false) String certno) {
        List<Enrollment> list = commonMapper.findEnrollment(idno, certno);
        if (list.isEmpty()) {
            return Result.ok(null);
        }
        return Result.ok(list.get(0));
    }



    // 注册验证Step4
    @PostMapping(UriView.REST_ENROLL_STEP4)
    @ResponseBody
    public Result<?> enrollStep4(@RequestBody EnrollStep4Form form) {
        // TODO 验证空

        // 证书上的验证
        int registerOrgId = form.registerOrg;
        String key = String.format(RedisKey.ORGS_ZHUCE, form.teachGrade, form.city);
        List<Organization> organizations = redisUtils.get(new TypeReference<List<Organization>>() {},
                key, () -> organizationMapper.findByOrgId(registerOrgId));

        if (organizations.isEmpty()) {
            return new Result(false, "未找到该机构");
        }

        Organization org = organizations.get(0);
        String proCode = form.proCode;
        if(proCode != null && Integer.parseInt(proCode) != 45){
            Calendar calendar = Calendar.getInstance();
            calendar.set(2008,8,1,0,0,0);
            Calendar cal = Calendar.getInstance();
            cal.setTime(form.certAssignDate);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            if(form.certAssignDate != null && !cal.getTime().before(calendar.getTime())){
                return new Result(false, "请仔细检查证书号码或证书签发日期是否有误");
            }
        }

        List<OrgBatch> orgBatchs = commonMapper.findOrgBatch(org.getId(), SignUpConstants.TYPE_ENROLL);
        if (orgBatchs.isEmpty()) {
            return new Result(false, "该机构目前未开展注册工作，请与该机构联系，了解其注册工作的时间安排");
        }

        // 注册验证
        OrgBatch orgBatch = orgBatchs.get(0);
        if(orgBatch != null &&  orgBatch.getProvinceBatch() != null){
            return new Result(false, "该机构当前注册工作已经结束");
        }

        if(orgBatch != null && orgBatch.getJoinIn() != null && !orgBatch.getJoinIn()){
            return new Result(false, "该机构目前未开展注册工作，请与该机构联系，了解其注册工作的时间安排");
        }

        if (orgBatch.getIsOpen() != null && !orgBatch.getIsOpen()) {
            return new Result(false,  "该机构注册工作目前未安排网上采集信息的时间，请与该机构联系，了解其注册工作的时间安排");
        }

        List<OrgBatchTime> orgBatchTimes = commonMapper.findOrgBatchTime(org.getId(),SignUpConstants.TYPE_ENROLL);
        if (!orgBatchTimes.isEmpty()) {
            StringBuffer buffer = new StringBuffer("该机构注册工作网上采集信息的时间段为:");
            String prependMsg = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月d日");
            for (OrgBatchTime batchTime : orgBatchTimes) {
                if (batchTime.getValidBeginDate().after(new Date())) {
                    prependMsg = "还未到网报时间，";
                }
                if (batchTime.getValidEndDate().before(new Date())) {
                    prependMsg = "网报时间已截止，";
                }
                buffer.append(dateFormat.format(batchTime.getValidBeginDate()));
                buffer.append("到");
                buffer.append(dateFormat.format(batchTime.getValidEndDate()));
                buffer.append(" ");
            }
            String errMsg = prependMsg + buffer.toString();
            return new Result(false, errMsg);
        }

        return Result.ok(form);
    }

    // 注册第七部验证
    @GetMapping(UriView.REST_ENROLL_STEP7)
    @ResponseBody
    public Result<?> enrollStep7(HttpServletRequest request,
                                 @RequestParam(value = "idno", defaultValue = "", required = false) String idno,
                                 @RequestParam(value = "certno", defaultValue = "", required = false) String certno) {
        //1.根据证件号和证书号查询enroll
        /*List<Enrollment> enrollments = commonMapper.findEnrollment(idno, certno);
        if (enrollments.isEmpty()) {
            return new Result(false, "验证失败 Enrollment为空");
        }*/
        /*Enrollment enroll = enrollments.get(0);*/
        Enrollment enroll = new Enrollment();
        enroll.setIdNo(idno);
        enroll.setCertNo(certno);

        if(enroll.getIdNo()!=null&&enroll.getCertNo()!=null) {
            List<Enrollment> status0 = commonMapper.findEnrollmentStatus0(idno, certno);
            if (!status0.isEmpty()) {
                return new Result(false, "定期注册证件号码与证书号码重复提交");
            } else if (enroll.isInRegistration()) {
                Registration reg = commonMapper.findRegistrationById(enroll.getRegId());
                if (reg == null) {
                    return new Result(false, "验证失败 Registration为空");
                }
                status0 = commonMapper.findEnrollmentStatus0(reg.getIdNo(), reg.getCertNo());
                if (!status0.isEmpty()) {
                    return new Result(false, "定期注册证件号码与证书号码重复提交");
                }
            } else if (enroll.isInHistory()) {
                HistoryValid historyValid = commonMapper.findHistoryValidById(enroll.getRegId());
                if (historyValid == null) {
                    return new Result(false, "验证失败 HistoryValid为空");
                }

                status0 = commonMapper.findEnrollmentStatus0(historyValid.getIdNo(), historyValid.getCertNo());
                if (!status0.isEmpty()) {
                    return new Result(false, "定期注册证件号码与证书号码重复提交");
                }
            }

            try {
                UserPortalLog userPortalLog = new UserPortalLog();
                userPortalLog.setUserId(idno);
                userPortalLog.setLogin(new Date());
                userPortalLog.setIp(getIp(request));
                userPortalLog.setType(UserPortalLog.ENROLL_SUBMIT);
                userPortalLog.setBrowserContent(BrowserUtils.getBrowserContent(request));
                userPortalLog.setBrowserName(BrowserUtils.getBrowserName(request));
                commonMapper.insertUserPortalLog(userPortalLog);
            } catch (Exception e) {
                logger.debug("插入用户操作日志失败！");
            }
        }

        return Result.ok(null);
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && ip.length() > 0) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CertTypeMapper certTypeMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private MajorMapper majorMapper;

    @Autowired
    private TechnicalJobMapper technicalJobMapper;

    @Autowired
    private LocalSetMapper localSetMapper;

    @Autowired
    private CommonMapper commonMapper;
}
