package com.xtuer.controller;

import com.alibaba.fastjson.TypeReference;
import com.xtuer.bean.EnrollStep4Form;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SignUpController {
    private static final int[] DICT_TYPES = {5, 7, 25, 22, 21, 2, 3, 9, 24, 6, 4, 10, 23};
    private static final String[] DICT_TYPENAMES = {"nation", "eduLevel", "schoolQuale", "workUnitType", "learnType",
            "normalMajor", "political", "pthLevel", "postQuale", "degree", "occupation", "idType", "teachGrade"};


    public static final int S_UNREVIEWED = 0;//待审核
    public static final int S_REVIEWED = 1;//已审核
    public static final int S_REVIEWED_FAIL = 2;//未通过
    public static final int T_LOSE = 1;// 丧失,时限：终生
    public static final int T_CANCEL2 = 2;// 撤销（品行不良、侮辱学生）,时限：5年
    public static final int T_CANCEL1 = 3;// 撤销（弄虚作假、骗取教师资格）,时限：5年
    public static final int T_CHEAT = 4;// 考试作弊：3年
    public static final int UN_DO_DURATION = 5;// 撤销处罚年限
    public static final int STATUS_UN_DO = 30; // 撤销注册
    public static final int STATUS_QUALIFIED = 19;// ***注册合格
    public static final int ENROLL_DURATION = 58;// 注册间隔是58个月
    public static final int DELETE_STATUS_NORMAL = 0; // 正常
    public static final int DELETE_STATUS_FORBID = 1; // 被限制
    public static final int DELETE_STATUS_DELETE = 2; // 删除
    public static final int T_GLOBAL = 5;//资格中心
    public static final int T_PROVINCE = 4;//省级机构
    public static final int T_CITY = 3;//市级机构
    public static final int T_COUNTY = 2;//县级机构
    public static final int T_LOCAL = 1;//确认机构
    public static final int T_ABROAD = 0;

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
    public Result<List<?>> getOrgByOrgType(@PathVariable("orgType") int orgType) {
        String key = String.format(RedisKey.ORGS_BY_ORGTYPE, orgType);

        List<?> organizations = null;
        if (orgType == 4) {
            organizations = redisUtils.get(new TypeReference<List<Province>>(){}, key, () -> organizationMapper.findByOrgTypeEq4());
        } else {
            organizations = redisUtils.get(new TypeReference<List<Organization>>() {}, key, () -> organizationMapper.findByOrgType(orgType));
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

        // 过滤时间
        Date now = new Date();
        List<LocalSet> localsets = new ArrayList<>(list.size());
        localsets.addAll(list.stream()
                .filter(l -> now.before(l.getEndDate()) && now.after(l.getValidBeginDate()) && now.before(l.getValidEndDate()))
                .collect(Collectors.toList()));

        return Result.ok(localsets);
    }

    // 确认点信息
    @GetMapping(UriView.REST_LOCALSET_INFO)
    @ResponseBody
    public Result<LocalSetInfo> getLocalSetInfo(@PathVariable("id") int id) {
        LocalSetInfo info = localSetMapper.findById(id);
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
        List<Major> majors = redisUtils.get(new TypeReference<List<Major>>(){}, key, () -> majorMapper.findByParentIdStatus1(parentId));
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
    public Result<Enrollhistory> findEnrollhistory(@RequestParam(value = "idno", defaultValue = "", required = false) String idno,
                                                   @RequestParam(value = "certno", defaultValue = "", required = false) String certno) {
        List<Enrollhistory> list = commonMapper.findEnrollhistory(idno, certno);
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

    // 注册验证Step3
    @GetMapping(UriView.REST_ENROLL_STEP3)
    @ResponseBody
    public Result<?> enrollStep3(@RequestParam(value = "idno", defaultValue = "", required = false) String idno,
                                                @RequestParam(value = "certno", defaultValue = "", required = false) String certno) {
        List<Limitation> limits = commonMapper.findLimitation(idno, certno);
        int limitationType = -1;
        if (!limits.isEmpty() && limits.get(0).getStatus() == S_REVIEWED) {
            Limitation limitation = limits.get(0);
            limitationType = limitation.getType();
            return new Result(false, "该证书受到限制，不允许注册");
        }

        if (limitationType == T_CANCEL1 || limitationType == T_CANCEL2) {
            return new Result(false, "该教师资格已被依法撤销");
        } else if (limitationType == T_LOSE) {
            return new Result(false, "该教师资格已被依法注销");
        } else if (limitationType == T_CHEAT) {
            return new Result(false, "您的教师资格无效");
        }

        HistoryValid historyValid = null;
        Enrollhistory enrollhistory = null;
        String _proCode = null;

        List<Enrollhistory> enrollhistorys = commonMapper.findEnrollhistory(idno, certno);
        if (!enrollhistorys.isEmpty()) {
            enrollhistory = enrollhistorys.get(0);
            if(enrollhistory.getEnrollTime() == null){
                return new Result(false, "注册历史数据存在异常");
            }

            Calendar expiredTime = Calendar.getInstance();
            expiredTime.setTime(enrollhistory.getEnrollTime());
            if(enrollhistory.getJudgmentStatus()!=null){
                switch (enrollhistory.getJudgmentStatus()) {
                    case STATUS_UN_DO:
                        expiredTime.add(Calendar.YEAR, UN_DO_DURATION);
                        if (expiredTime.getTime().after(new Date())) {
                            String errorMsg = "该证书于" + formatDate(enrollhistory.getEnrollTime(), "yyyy年M月d日") + "已被撤销注册，"
                                    + UN_DO_DURATION + "年内不能再注册。不能继续下一步!";
                            return new Result(false, errorMsg);
                        }
                        break;
                    // STATUS_QUALIFIED = 19;// ***注册合格
                    case STATUS_QUALIFIED:
                        //三-3、“注册合格”且与上次注册日期间隔短于58个月
                        expiredTime.add(Calendar.MONTH, ENROLL_DURATION);
                        if (expiredTime.getTime().after(new Date())) {
                            String errorMsg = "该证书已于" + formatDate(enrollhistory.getEnrollTime(), "yyyy年M月d日") + "注册，"
                                    + "5年内无需再注册。";
                            return new Result(false, errorMsg);
                        }
                        break;
                }
            }
        }

        List<Enrollment> enrollments = commonMapper.findEnrollment(idno, certno);
        if (!enrollments.isEmpty()) {
            return new Result(false, "您已经填写了申报信息，请直接登陆查看或修改申报信息；");
        }

        List<HistoryValid> historyValids = commonMapper.findHistoryValid(idno, certno);
        if (!historyValids.isEmpty()) {
            historyValid = historyValids.get(0);
        }

        Enrollment enrollment = new Enrollment();
        Registration registration = new Registration();

        enrollment.setCertNo(certno);
        enrollment.setIdNo(idno);

        if (historyValid == null ) {
            enrollment.setInHistory(false);
            List<Registration> registrations = commonMapper.findRegistration(idno, certno);
            if (!registrations.isEmpty()) {
                Registration r = registrations.get(0);
                enrollment.setInRegistration(Boolean.TRUE);
                registration.setIdNo(r.getIdNo());
                registration.setCertNo(r.getCertNo());
                registration.setCertType(r.getCertType());
                enrollment.setInRegistration(true);
            }else{
                int year = 0;
                if (certno.length() > 15) {
                    year = Integer.parseInt(certno.substring(0, 4));
                }else{
                    year = 1900 + Integer.parseInt(certno.substring(0, 2));
                }

                if (year >= 2012 || year < 1996) {
                    return new Result(false, "无此教师资格,请与发证机关确认");
                }

                if(year>=2008 && year<2012){
                    _proCode = certno.substring(4, 6);//这个地方是省份编码,判断是否是广西省,第四步用到
                }
                enrollment.setInRegistration(Boolean.FALSE);
            }
            enrollment.setEnrollNum(1);
        }else{
            if(historyValid.getDeleteStatus() == DELETE_STATUS_FORBID){
                return new Result(false, "该数据已受限，不能进行定期注册报名!");
            }

            if(historyValid.getDeleteStatus() == DELETE_STATUS_DELETE){
                return new Result(false, "该证书不存在，请您仔细核对信息!");
            }

            // 此时应该返回historyValid的所有信息在第四步上端显示;
            registration.setCertType(historyValid.getCertType());
            registration.setName(historyValid.getName());
            enrollment.setInHistory(true);
            if(enrollhistory == null){
                enrollment.setEnrollNum(1);
            }else{
                if (enrollhistory.getJudgmentStatus() == STATUS_QUALIFIED) {
                    enrollment.setEnrollNum(enrollhistory.getEnrollNum() + 1);
                } else {
                    enrollment.setEnrollNum(enrollhistory.getEnrollNum());
                }
            }
        }

        Map<String, Object> map = new  HashMap<>();
        map.put("enrollhistory", enrollhistory);
        map.put("enrollment", enrollment);
        map.put("historyValid", historyValid);
        map.put("registration", registration);
        map.put("_proCode", _proCode);
        return Result.ok(map);
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

        List<OrgBatch> orgBatchs = commonMapper.findOrgBatch(org.getId());
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

        if (orgBatch.getOpen() != null && !orgBatch.getOpen()) {
            return new Result(false,  "该机构注册工作目前未安排网上采集信息的时间，请与该机构联系，了解其注册工作的时间安排");
        }

        List<OrgBatchTime> orgBatchTimes = commonMapper.findOrgBatchTime(org.getId());
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


    private String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
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
