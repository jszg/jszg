package com.xtuer.controller;

import com.xtuer.bean.Result;
import com.xtuer.constant.RedisKey;
import com.xtuer.constant.UriView;
import com.xtuer.dto.CertType;
import com.xtuer.dto.City;
import com.xtuer.dto.College;
import com.xtuer.dto.Dict;
import com.xtuer.dto.Major;
import com.xtuer.dto.Organization;
import com.xtuer.dto.Province;
import com.xtuer.dto.Subject;
import com.xtuer.dto.TechnicalJob;
import com.xtuer.mapper.CertTypeMapper;
import com.xtuer.mapper.CityMapper;
import com.xtuer.mapper.CollegeMapper;
import com.xtuer.mapper.DictMapper;
import com.xtuer.mapper.MajorMapper;
import com.xtuer.mapper.OrganizationMapper;
import com.xtuer.mapper.ProvinceMapper;
import com.xtuer.mapper.SubjectMapper;
import com.xtuer.mapper.TechnicalJobMappler;
import com.xtuer.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SignUpController {
    private static final int[] DICT_TYPES = {5, 7, 25, 22, 21, 2, 3, 9, 24, 6, 4, 10};
    private static final String[] DICT_TYPENAMES = {"nation", "eduLevel", "schoolQuale", "workUnitType", "learnType",
            "normalMajor", "political", "pthLevel", "postQuale", "degree", "occupation", "idType"};

    // 所有资格种类
    @GetMapping(UriView.REST_CERT_TYPE)
    @ResponseBody
    public Result<List<CertType>> getCertTypes() {
        List<CertType> types = redisUtils.get(List.class, RedisKey.CERT_TYPES, () -> certTypeMapper.findAll());
        return Result.ok(types);
    }

    // 所有的省
    @GetMapping(UriView.REST_PROVINCES)
    @ResponseBody
    public Result<List<Province>> getProvinces() {
        List<Province> provinces = redisUtils.get(List.class, RedisKey.PROVINCES, () -> provinceMapper.findAll());
        return Result.ok(provinces);
    }

    // 省下面所有的市
    @GetMapping(UriView.REST_CITIES_BY_PROVINCE)
    @ResponseBody
    public Result<List<City>> getCities(@PathVariable("provinceId") Integer id) {
        String key = String.format(RedisKey.CITIES, id);
        List<City> cities = redisUtils.get(List.class, key, () -> cityMapper.findByParentId(id));

        return Result.ok(cities);
    }

    // 市或省下面的所有的认定机构
    // 1-5 查市下面的所有的认定机构
    // 6-7 查省下面的所有的认定机构
    @GetMapping(UriView.REST_ORGS_BY_CITY_AND_CERT_TYPE)
    @ResponseBody
    public Result<List<Organization>> getOrgByCity(@PathVariable("cityId") Integer cityId, @PathVariable("certTypeId") Integer certTypeId) {
        List<Organization> orgs = Collections.emptyList();
        String key = String.format(RedisKey.ORGS, cityId, certTypeId);

        if (certTypeId >= 1 && certTypeId <= 5) {
            // 1-5 查市
            orgs = redisUtils.get(List.class, key, () -> organizationMapper.findByCertTypeAndCity(cityId, certTypeId));
        } else if (certTypeId >= 6 && certTypeId <= 7) {
            // 6-7 查省
            orgs = redisUtils.get(List.class, key, () -> organizationMapper.findByCertTypeAndProvince(cityId, certTypeId));
        }

        return Result.ok(orgs);
    }


    // 证书上的机构 和选择的证书签发日期有关(CertTypeOrgTreeController)
    // 父机构
    @GetMapping(UriView.REST_ORGS_BY_ORGTYPE)
    @ResponseBody
    public Result<List<Organization>> getOrgByOrgType(@PathVariable("orgType") int orgType) {
        String key = String.format(RedisKey.ORGS_BY_ORGTYPE, orgType);

        List<Organization> organizations = Collections.emptyList();
        if (orgType == 4) {
            organizations = redisUtils.get(List.class, key, () -> organizationMapper.findByOrgTypeEq4());
        } else {
            organizations = redisUtils.get(List.class, key, () -> organizationMapper.findByOrgType(orgType));
        }
        return Result.ok(organizations);
    }

    // 子机构
    @GetMapping(UriView.REST_ORGS_BY_PARENT)
    @ResponseBody
    public Result<List<Organization>> getOrgByParentId(@PathVariable("parentId") int parentId) {
        String key = String.format(RedisKey.ORGS_BY_PARENT, parentId);
        List<Organization> list = redisUtils.get(List.class, key, () -> organizationMapper.findByParentId(parentId));
        return Result.ok(list);
    }

    // 注册机构
    @GetMapping(UriView.REST_ORGS_BY_CITY)
    @ResponseBody
    public Result<List<Organization>> getOrgByCity(@PathVariable("cityId") int cityId) {
        String key = String.format(RedisKey.ORGS_BY_CITY, cityId);
        List<Organization> list = redisUtils.get(List.class, key, () -> organizationMapper.findByParentIdAndOrgTypeNot1(cityId));
        return Result.ok(list);
    }

    // 省下面的第一级任教学科
    @GetMapping(UriView.REST_SUBJECTS_ROOT)
    @ResponseBody
    public Result<List<Subject>> getRootSubjects(@PathVariable("provinceId") int provinceId, @PathVariable("certTypeId") int certTypeId) {
        String key = String.format(RedisKey.SUBJECTS_ROOT, provinceId, certTypeId);
        List<Subject> subjects = redisUtils.get(List.class, key, () -> subjectMapper.findRoots(provinceId, certTypeId));

        return Result.ok(subjects);
    }

    // 省下面指定父节点的任教学科
    @GetMapping(UriView.REST_SUBJECTS_CHILDREN)
    @ResponseBody
    public Result<List<Subject>> getChildrenSubjects(@PathVariable("provinceId") int provinceId, @PathVariable("parentId") int parentId) {
        String key = String.format(RedisKey.SUBJECTS_CHILDREN, provinceId, parentId);
        List<Subject> subjects = redisUtils.get(List.class, key, () -> subjectMapper.findByParentAndProvince(parentId, provinceId));

        return Result.ok(subjects);
    }

    // 证书上的任教学科 (OrgCertTypeSubjectTreeController)
    // 父节点
    @GetMapping(UriView.REST_SUBJECTS_BY_CERT_TYPE)
    @ResponseBody
    public Result<List<Subject>> getSubjectByCertType(@PathVariable("certTypeId") int certTypeId) {
        String key = String.format(RedisKey.SUBJECTS_BY_CERTTYPE, certTypeId);
        List<Subject> list = redisUtils.get(List.class, key, () -> subjectMapper.findByCertType(certTypeId));
        return Result.ok(list);
    }

    // 子节点
    @GetMapping(UriView.REST_SUBJECTS_BY_PARENT)
    @ResponseBody
    public Result<List<Subject>> getSubjectByParent(@PathVariable("parentId") int parentId) {
        String key = String.format(RedisKey.SUBJECTS_BY_PARENT, parentId);
        List<Subject> subjects = redisUtils.get(List.class, key, () -> subjectMapper.findByParent(parentId));
        return Result.ok(subjects);
    }

    // 现任教学科 父节点, 子节点@see getChildrenSubjects
    @GetMapping(UriView.REST_SUBJECTS_TEASUBJECT)
    @ResponseBody
    public Result<List<Subject>> getTeaSubjects(@PathVariable("provinceId") int provinceId, @PathVariable("subjectType") int subjectType) {
        String key = String.format(RedisKey.SUBJECTS_TEASUBJECT, subjectType, provinceId);
        List<Subject> list = redisUtils.get(List.class, key, () -> subjectMapper.findBySubjectTypeAndProvince(subjectType, provinceId));
        return Result.ok(list);
    }

    // 按照字典类型查询字典数据
    @GetMapping(UriView.REST_DICTS_BY_DICTTYPE)
    @ResponseBody
    public Result<List<Dict>> getDicts(@PathVariable("dictTypeId") int dictTypeId) {
        String key = String.format(RedisKey.DICTS_BY_TYPE, dictTypeId);
        List<Dict> dicts = redisUtils.get(List.class, key, () -> dictMapper.findByDictType(dictTypeId));
        return Result.ok(dicts);
    }

    // 所有字典，按类型分类
    @GetMapping(UriView.REST_DICTS)
    @ResponseBody
    public Result<Map<String, List<Object>>> getDicts() {
        String key = RedisKey.DICTS;
        Map<String, List<Object>> dicts  = redisUtils.get(Map.class, key, () -> {
            Map<String, List<Object>> map = new HashMap<String, List<Object>>();
            for (int i = 0; i < DICT_TYPES.length; i++) {
                final int type = DICT_TYPES[i];
                String subkey = String.format(RedisKey.DICTS_BY_TYPE, type);
                map.put(DICT_TYPENAMES[i], redisUtils.get(List.class, subkey, () -> dictMapper.findByDictType(type)));
            }

            map.put(RedisKey.PROVINCES, redisUtils.get(List.class, RedisKey.PROVINCES, () -> provinceMapper.findAll()));
            map.put(RedisKey.CERT_TYPES, redisUtils.get(List.class, RedisKey.CERT_TYPES, () -> certTypeMapper.findAll()));
            return map;
        });
        return Result.ok(dicts);
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
    public Result<List<Dict>> getTeaGrades() {
        String key = RedisKey.TEAGRADES;
        List<Dict> list = redisUtils.get(List.class, key, () -> dictMapper.findTeaGrade());
        return Result.ok(list);
    }

    // 所有学校
    @GetMapping(UriView.REST_COLLEGES)
    @ResponseBody
    public Result<List<College>> getColleges() {
        String key = RedisKey.COLLEGES;
        List<College> colleges = redisUtils.get(List.class, key, () -> collegeMapper.findAll());
        return Result.ok(colleges);
    }

    // 按省份找学校
    @GetMapping(UriView.REST_COLLEGES_BY_PROVINCE)
    @ResponseBody
    public Result<List<College>> getCollegesByProvinceId(@PathVariable("provinceId") int provinceId) {
        String key = String.format(RedisKey.COLLEGES_BY_PROVINCE, provinceId);
        List<College> colleges = redisUtils.get(List.class, key, () -> collegeMapper.findByProvinceId(provinceId));
        return Result.ok(colleges);
    }

    // 注册的根节点
    @GetMapping(UriView.REST_ZHUCE_MAJOR_PARENT)
    @ResponseBody
    public Result<List<Major>> getZhuceRootMajors() {
        String key = RedisKey.MAJORS_ZHUCE_ROOT;
        List<Major> majors = redisUtils.get(List.class, key, () -> majorMapper.findRoot());
        return Result.ok(majors);
    }

    // 注册的子节点
    @GetMapping(UriView.REST_ZHUCE_MAJOR_CHILDREN)
    @ResponseBody
    public Result<List<Major>> getZhuceChildrenMajors(@PathVariable("parentId") int parentId) {
        String key = String.format(RedisKey.MAJORS_ZHUCE_CHILREN, parentId);
        List<Major> majors = redisUtils.get(List.class, key, () -> majorMapper.findByParentId(parentId));
        return Result.ok(majors);
    }

    // 认定的根节点
    @GetMapping(UriView.REST_RENDING_MAJOR_PARENT)
    @ResponseBody
    public Result<List<Major>> getRendingRootMajors(@PathVariable("certTypeId") int certTypeId, @PathVariable("eduLevelId") int eduLevelId) {
        String key = String.format(RedisKey.MAJORS_RENDING_ROOT, certTypeId, eduLevelId);
        List<Major> majors = redisUtils.get(List.class, key, () -> majorMapper.findByCertTypeIdAndEduLevelId(certTypeId, eduLevelId));
        return Result.ok(majors);
    }

    // 认定的子节点
    @GetMapping(UriView.REST_RENDING_MAJOR_CHILDREN)
    @ResponseBody
    public Result<List<Major>> getRendingChildrenMajors(@PathVariable("provinceId") int provinceId, @PathVariable("parentId") int parentId) {
        String key = String.format(RedisKey.MAJORS_RENDING_CHILDREN, provinceId, parentId);
        List<Major> majors = redisUtils.get(List.class, key, () -> majorMapper.findByParentIdAndProvince(parentId, provinceId));
        return Result.ok(majors);
    }

    // 专业技术职务根节点
    @GetMapping(UriView.REST_TECHNICAL_JOB_ROOT)
    @ResponseBody
    public Result<List<TechnicalJob>> getRootTechnicalJobs() {
        String key = RedisKey.TECHNICALJOBS;
        List<TechnicalJob> jobs = redisUtils.get(List.class, key, () -> technicalJobMappler.findRoots());
        return Result.ok(jobs);
    }

    // 专业技术职务子节点
    @GetMapping(UriView.REST_TECHNICAL_JOB_CHILDREN)
    @ResponseBody
    public Result<List<TechnicalJob>> getChildrenTechnicalJobs(@PathVariable("parentId") int parentId) {
        String key = String.format(RedisKey.TECHNICAL_JOB_CHILDREN, parentId);
        List<TechnicalJob> jobs = redisUtils.get(List.class, key, () -> technicalJobMappler.findByParent(parentId));
        return Result.ok(jobs);
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
    private TechnicalJobMappler technicalJobMappler;
}
