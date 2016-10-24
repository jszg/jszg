package com.xtuer.controller;

import com.alibaba.fastjson.TypeReference;
import com.xtuer.bean.Result;
import com.xtuer.constant.RedisKey;
import com.xtuer.constant.UriView;
import com.xtuer.dto.*;
import com.xtuer.mapper.*;
import com.xtuer.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SignUpController {
    private static final int[] DICT_TYPES = {5, 7, 25, 22, 21, 2, 3, 9, 24, 6, 4, 10, 23};
    private static final String[] DICT_TYPENAMES = {"nation", "eduLevel", "schoolQuale", "workUnitType", "learnType",
            "normalMajor", "political", "pthLevel", "postQuale", "degree", "occupation", "idType", "teachGrade"};

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
    @GetMapping(UriView.REST_ORGS_RENDING)
    @ResponseBody
    public Result<List<Organization>> getOrgByCity(@PathVariable("provinceId") int provinceId,
                                                   @PathVariable("cityId") int cityId,
                                                   @PathVariable("certTypeId") int certTypeId) {
        List<Organization> orgs = Collections.emptyList();
        String key = String.format(RedisKey.ORGS_RENDING, provinceId, cityId, certTypeId);
        orgs = redisUtils.get(new TypeReference<List<Organization>>() {}, key,
                () -> organizationMapper.findByProvinceAndCityAndCertTypeId(provinceId, cityId, certTypeId));
        return Result.ok(orgs);
    }

    // 证书上的机构 和选择的证书签发日期有关(CertTypeOrgTreeController)
    // 父机构
    @GetMapping(UriView.REST_ORGS_BY_ORGTYPE)
    @ResponseBody
    public Result<List<Organization>> getOrgByOrgType(@PathVariable("orgType") int orgType) {
        String key = String.format(RedisKey.ORGS_BY_ORGTYPE, orgType);

        List<Organization> organizations = Collections.emptyList();
        TypeReference<List<Organization>> typeReference = new TypeReference<List<Organization>>() {};

        if (orgType == 4) {
            organizations = redisUtils.get(typeReference, key, () -> organizationMapper.findByOrgTypeEq4());
        } else {
            organizations = redisUtils.get(typeReference, key, () -> organizationMapper.findByOrgType(orgType));
        }
        return Result.ok(organizations);
    }

    // 子机构
    @GetMapping(UriView.REST_ORGS_BY_PARENT)
    @ResponseBody
    public Result<List<Organization>> getOrgByParentId(@PathVariable("parentId") int parentId) {
        String key = String.format(RedisKey.ORGS_BY_PARENT, parentId);
        List<Organization> list = redisUtils.get(new TypeReference<List<Organization>>(){}, key, () -> organizationMapper.findByParentId(parentId));
        return Result.ok(list);
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

    // 证书上的任教学科 (OrgCertTypeSubjectTreeController)
    // 父节点
    @GetMapping(UriView.REST_SUBJECTS_BY_CERT_TYPE)
    @ResponseBody
    public Result<List<Subject>> getSubjectByCertType(@PathVariable("certTypeId") int certTypeId) {
        String key = String.format(RedisKey.SUBJECTS_BY_CERTTYPE, certTypeId);
        List<Subject> list = redisUtils.get(new TypeReference<List<Subject>>(){}, key, () -> subjectMapper.findByCertType(certTypeId));
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

            TypeReference<List<Dict>> dictReference = new TypeReference<List<Dict>>() {};
            for (int i = 0; i < DICT_TYPES.length; i++) {
                final int type = DICT_TYPES[i];
                String subkey = String.format(RedisKey.DICTS_BY_TYPE, type);
                map.put(DICT_TYPENAMES[i], redisUtils.get(dictReference, subkey, () -> dictMapper.findByDictType(type)));
            }

            map.put(RedisKey.PROVINCES, redisUtils.get(new TypeReference<List<Province>>(){}, RedisKey.PROVINCES, () -> provinceMapper.findAll()));
            map.put(RedisKey.CERT_TYPES, redisUtils.get(new TypeReference<List<CertType>>(){}, RedisKey.CERT_TYPES, () -> certTypeMapper.findAll()));
            return map;
        });
        return Result.ok(dicts);
    }

    // 确认点
    @GetMapping(UriView.REST_LOCALSETS)
    @ResponseBody
    public Result<List<LocalSet>> getLocalsets(@RequestParam("orgId") int orgId) {
        String key = String.format(RedisKey.LOCALSETS, orgId);
        List<LocalSet> list = redisUtils.get(new TypeReference<List<LocalSet>>() {}, key,
                () -> localSetMapper.findByOrgId(orgId));
        return Result.ok(list);
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
        List<Dict> list = redisUtils.get(new TypeReference<List<Dict>>(){}, key, () -> dictMapper.findTeaGrades());
        return Result.ok(list);
    }

    // 最高学历:认定报名中,最高学历eduLevels是和资格种类关联的
    @GetMapping(UriView.REST_EDULEVELS)
    @ResponseBody
    public Result<List<Dict>> getEduLevels(@PathVariable("certTypeId") int certTypeId) {
        String key = String.format(RedisKey.EDULEVELS, certTypeId);
        List<Dict> dicts = redisUtils.get(new TypeReference<List<Dict>>(){}, key, () -> dictMapper.findEduLevels(certTypeId));
        return Result.ok(dicts);
    }

    //最高学位
    @GetMapping(UriView.REST_ACADEMICDEGREE)
    @ResponseBody
    public Result<List<Dict>> getAcademicDegrees(@PathVariable("certTypeId") int certTypeId, @PathVariable("eduLevel") int eduLevel) {
        String key = String.format(RedisKey.ACADEMICDEGREE, certTypeId, eduLevel);
        List<Dict> dicts = redisUtils.get(new TypeReference<List<Dict>>(){}, key, () -> dictMapper.findAcademicDegrees(certTypeId, eduLevel));
        return Result.ok(dicts);
    }


    // 所有学校
    @GetMapping(UriView.REST_COLLEGES)
    @ResponseBody
    public Result<List<College>> getColleges() {
        String key = RedisKey.COLLEGES;
        List<College> colleges = redisUtils.get(new TypeReference<List<College>>(){}, key, () -> collegeMapper.findAll());
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
    @GetMapping(UriView.REST_RENDING_MAJOR_PARENT)
    @ResponseBody
    public Result<List<Major>> getRendingRootMajors() {
        String key = RedisKey.MAJORS_ZHUCE_ROOT;
        List<Major> majors = redisUtils.get(new TypeReference<List<Major>>(){}, key, () -> majorMapper.findRoot());
        return Result.ok(majors);
    }

    // 注册的根节点
    @GetMapping(UriView.REST_ZHUCE_MAJOR_PARENT)
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
    @GetMapping(UriView.REST_LIMITATIONS)
    @ResponseBody
    public Result<List<Limitation>> findLimitations(@RequestParam(value = "idno", defaultValue = "", required = false) String idno,
                                                    @RequestParam(value = "certno", defaultValue = "", required = false) String certno) {
        List<Limitation> limitations = limitationMapper.findByIdnoAndCertno(idno, certno);
        return Result.ok(limitations);
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
    private LimitationMapper limitationMapper;
}
