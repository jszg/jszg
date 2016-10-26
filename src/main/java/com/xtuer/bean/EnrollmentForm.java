package com.xtuer.bean;

import java.util.Date;

/**
 * 这个是在保存注册信息时可能用到的字段
 *
 * Created by microacup on 2016/10/26.
 */
public class EnrollmentForm {
    private String idNo;//证件号码
    private String certNo;//证书号码
    private Integer idType;//证件类型
    private Integer enrollNum;//注册类型
    private Integer inHistory;//是否在认定历史库中
    private Integer inRegistration;//是否在认定表中
    private Integer degree;//最高学位
    private Integer eduLevel;//最高学历
    private Integer learnType;//最高学历学习形式
    private Integer normalMajor;//最高学历专业类别
    private Integer graduateSchool;//最高学历毕业学校
    private Integer major;//最高学历所学专业
    private Date graduaTime;//最高学历毕业时间
    private Integer political;//政治面貌
    private Integer workUnitType;//任教学校所在地
    private Integer schoolQuale;//现任教学校性质
    private String workUnit;//现任教学校
    private Date workDate;//现任教学校聘用起始日期
    private Integer postQuale;//岗位性质
    private Integer pthLevel;//普通话水平
    private Date beginWorkYear;//开始参加工作时间
    private Integer techniqueJob;//教师职务（职称）
    private String birthPlace;//出生地
    private String residence;//户籍所在地
    private String address;//通讯地址
    private String zipCole;//通讯地的邮编
    private String phone;//联系电话
    private String cellphone;//手机
    private String tmpPhoto;//照片
    private Integer confirmStatus;//确认状态
    private Integer status;//初审注册状态
    private Integer reCheckStatus;//复核注册状态
    private Integer JudgmentStatus;//终审注册状态
    private String ip;//ip
    private Integer provinceId;//省
    private Integer city;//市
    private Integer org;//机构
    private Integer teaGrade;//现任教学段
    private Long localeId;//确认点
    private Integer teaSubject;//现任教学科
    private Integer enrollBatch;//注册批次
    private String enail;//电子邮件
    private String password;//密码
    private Date lastModify;//最后修改时间
    private String lastModifier;//最后修改人
    private Integer deleteStatus;//删除标记
    private Integer countyId;//所在县

    public String getIdNo() {
        return idNo;
    }

    public EnrollmentForm setIdNo(String idNo) {
        this.idNo = idNo;
        return this;
    }

    public String getCertNo() {
        return certNo;
    }

    public EnrollmentForm setCertNo(String certNo) {
        this.certNo = certNo;
        return this;
    }

    public Integer getIdType() {
        return idType;
    }

    public EnrollmentForm setIdType(Integer idType) {
        this.idType = idType;
        return this;
    }

    public Integer getEnrollNum() {
        return enrollNum;
    }

    public EnrollmentForm setEnrollNum(Integer enrollNum) {
        this.enrollNum = enrollNum;
        return this;
    }

    public Integer getInHistory() {
        return inHistory;
    }

    public EnrollmentForm setInHistory(Integer inHistory) {
        this.inHistory = inHistory;
        return this;
    }

    public Integer getInRegistration() {
        return inRegistration;
    }

    public EnrollmentForm setInRegistration(Integer inRegistration) {
        this.inRegistration = inRegistration;
        return this;
    }

    public Integer getDegree() {
        return degree;
    }

    public EnrollmentForm setDegree(Integer degree) {
        this.degree = degree;
        return this;
    }

    public Integer getEduLevel() {
        return eduLevel;
    }

    public EnrollmentForm setEduLevel(Integer eduLevel) {
        this.eduLevel = eduLevel;
        return this;
    }

    public Integer getLearnType() {
        return learnType;
    }

    public EnrollmentForm setLearnType(Integer learnType) {
        this.learnType = learnType;
        return this;
    }

    public Integer getNormalMajor() {
        return normalMajor;
    }

    public EnrollmentForm setNormalMajor(Integer normalMajor) {
        this.normalMajor = normalMajor;
        return this;
    }

    public Integer getGraduateSchool() {
        return graduateSchool;
    }

    public EnrollmentForm setGraduateSchool(Integer graduateSchool) {
        this.graduateSchool = graduateSchool;
        return this;
    }

    public Integer getMajor() {
        return major;
    }

    public EnrollmentForm setMajor(Integer major) {
        this.major = major;
        return this;
    }

    public Date getGraduaTime() {
        return graduaTime;
    }

    public EnrollmentForm setGraduaTime(Date graduaTime) {
        this.graduaTime = graduaTime;
        return this;
    }

    public Integer getPolitical() {
        return political;
    }

    public EnrollmentForm setPolitical(Integer political) {
        this.political = political;
        return this;
    }

    public Integer getWorkUnitType() {
        return workUnitType;
    }

    public EnrollmentForm setWorkUnitType(Integer workUnitType) {
        this.workUnitType = workUnitType;
        return this;
    }

    public Integer getSchoolQuale() {
        return schoolQuale;
    }

    public EnrollmentForm setSchoolQuale(Integer schoolQuale) {
        this.schoolQuale = schoolQuale;
        return this;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public EnrollmentForm setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
        return this;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public EnrollmentForm setWorkDate(Date workDate) {
        this.workDate = workDate;
        return this;
    }

    public Integer getPostQuale() {
        return postQuale;
    }

    public EnrollmentForm setPostQuale(Integer postQuale) {
        this.postQuale = postQuale;
        return this;
    }

    public Integer getPthLevel() {
        return pthLevel;
    }

    public EnrollmentForm setPthLevel(Integer pthLevel) {
        this.pthLevel = pthLevel;
        return this;
    }

    public Date getBeginWorkYear() {
        return beginWorkYear;
    }

    public EnrollmentForm setBeginWorkYear(Date beginWorkYear) {
        this.beginWorkYear = beginWorkYear;
        return this;
    }

    public Integer getTechniqueJob() {
        return techniqueJob;
    }

    public EnrollmentForm setTechniqueJob(Integer techniqueJob) {
        this.techniqueJob = techniqueJob;
        return this;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public EnrollmentForm setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
        return this;
    }

    public String getResidence() {
        return residence;
    }

    public EnrollmentForm setResidence(String residence) {
        this.residence = residence;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public EnrollmentForm setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getZipCole() {
        return zipCole;
    }

    public EnrollmentForm setZipCole(String zipCole) {
        this.zipCole = zipCole;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public EnrollmentForm setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getCellphone() {
        return cellphone;
    }

    public EnrollmentForm setCellphone(String cellphone) {
        this.cellphone = cellphone;
        return this;
    }

    public String getTmpPhoto() {
        return tmpPhoto;
    }

    public EnrollmentForm setTmpPhoto(String tmpPhoto) {
        this.tmpPhoto = tmpPhoto;
        return this;
    }

    public Integer getConfirmStatus() {
        return confirmStatus;
    }

    public EnrollmentForm setConfirmStatus(Integer confirmStatus) {
        this.confirmStatus = confirmStatus;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public EnrollmentForm setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getReCheckStatus() {
        return reCheckStatus;
    }

    public EnrollmentForm setReCheckStatus(Integer reCheckStatus) {
        this.reCheckStatus = reCheckStatus;
        return this;
    }

    public Integer getJudgmentStatus() {
        return JudgmentStatus;
    }

    public EnrollmentForm setJudgmentStatus(Integer judgmentStatus) {
        JudgmentStatus = judgmentStatus;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public EnrollmentForm setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public EnrollmentForm setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
        return this;
    }

    public Integer getCity() {
        return city;
    }

    public EnrollmentForm setCity(Integer city) {
        this.city = city;
        return this;
    }

    public Integer getOrg() {
        return org;
    }

    public EnrollmentForm setOrg(Integer org) {
        this.org = org;
        return this;
    }

    public Integer getTeaGrade() {
        return teaGrade;
    }

    public EnrollmentForm setTeaGrade(Integer teaGrade) {
        this.teaGrade = teaGrade;
        return this;
    }

    public Long getLocaleId() {
        return localeId;
    }

    public EnrollmentForm setLocaleId(Long localeId) {
        this.localeId = localeId;
        return this;
    }

    public Integer getTeaSubject() {
        return teaSubject;
    }

    public EnrollmentForm setTeaSubject(Integer teaSubject) {
        this.teaSubject = teaSubject;
        return this;
    }

    public Integer getEnrollBatch() {
        return enrollBatch;
    }

    public EnrollmentForm setEnrollBatch(Integer enrollBatch) {
        this.enrollBatch = enrollBatch;
        return this;
    }

    public String getEnail() {
        return enail;
    }

    public EnrollmentForm setEnail(String enail) {
        this.enail = enail;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public EnrollmentForm setPassword(String password) {
        this.password = password;
        return this;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public EnrollmentForm setLastModify(Date lastModify) {
        this.lastModify = lastModify;
        return this;
    }

    public String getLastModifier() {
        return lastModifier;
    }

    public EnrollmentForm setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
        return this;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public EnrollmentForm setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
        return this;
    }

    public Integer getCountyId() {
        return countyId;
    }

    public EnrollmentForm setCountyId(Integer countyId) {
        this.countyId = countyId;
        return this;
    }
}
