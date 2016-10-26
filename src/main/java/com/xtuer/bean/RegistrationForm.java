package com.xtuer.bean;

import java.util.Date;

/**
 * 这是认定需要用到的字段
 *
 * Created by microacup on 2016/10/26.
 */
public class RegistrationForm {
    private String idNo;//证件号码
    private String certNo;//证书号码
    private Integer idType;//证件类型
    private Date birthday;//出生日期
    private Integer sex;//性别
    private Integer subjectId;//任教学科
    private Integer orgId;//机构
    private Integer certTypeId;//资格种类
    private Integer nationId;//资格种类
    private String orgName;//机构名称
    private Integer provinceId;//省
    private Integer cityId;//市
    private Integer occupation;//现从事职业(Dictionary occ = dictionaryService.findByTypeAndCode(DictionaryType.ID_OCCUPATION, "20"))
    private Integer enrollProBatchId;//注册的省级批次(ProvinceBatch pb = provinceBatchService.getValidProvinceBatch(enroll.getProvince().getId(),				PlanHelper.TYPE_ENROLL);
    private Integer certBatchId;//认定批次（int year = CertUtils.getCertYearFromRegistration(reg);			CertBatch cb = certBatchService.createAutoCertBatchByYear(year);）
    private String ip;//ip
    private String address;
    private String Address;//通讯地址
    private String archiveNo;//档案号
    private String birthPlace;//出生地
    private String cellphone;//手机号码
    private Integer degree; //最高学位
    private Integer eduLevel;//最高学历
    private String Email;//密码找回邮箱
    private String password;//密码
    private Integer graduateSchool;//毕业学校
    private Integer learnType;//学习形式
    private Integer major;//所学专业
    private Integer normalMajor;//专业类别
    private String phone;//联系电话
    private Integer political;//政治面貌
    private Integer pthLevel;//普通话水平
    private String residence;//户籍所在地
    private Integer techniqueJob;//专业技术职务
    private String workUnits;//工作单位
    private String zipCole;//邮编
    private Integer exam;//考试类型
    private Integer status;//认定状态
    private Integer dataFrom;//数据来源
    private Integer deleteStatus;//数据状态
    private Date triggerTime;//触发时间

    public String getIdNo() {
        return idNo;
    }

    public RegistrationForm setIdNo(String idNo) {
        this.idNo = idNo;
        return this;
    }

    public String getCertNo() {
        return certNo;
    }

    public RegistrationForm setCertNo(String certNo) {
        this.certNo = certNo;
        return this;
    }

    public Integer getIdType() {
        return idType;
    }

    public RegistrationForm setIdType(Integer idType) {
        this.idType = idType;
        return this;
    }

    public Date getBirthday() {
        return birthday;
    }

    public RegistrationForm setBirthday(Date birthday) {
        this.birthday = birthday;
        return this;
    }

    public Integer getSex() {
        return sex;
    }

    public RegistrationForm setSex(Integer sex) {
        this.sex = sex;
        return this;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public RegistrationForm setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public RegistrationForm setOrgId(Integer orgId) {
        this.orgId = orgId;
        return this;
    }

    public Integer getCertTypeId() {
        return certTypeId;
    }

    public RegistrationForm setCertTypeId(Integer certTypeId) {
        this.certTypeId = certTypeId;
        return this;
    }

    public Integer getNationId() {
        return nationId;
    }

    public RegistrationForm setNationId(Integer nationId) {
        this.nationId = nationId;
        return this;
    }

    public String getOrgName() {
        return orgName;
    }

    public RegistrationForm setOrgName(String orgName) {
        this.orgName = orgName;
        return this;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public RegistrationForm setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
        return this;
    }

    public Integer getCityId() {
        return cityId;
    }

    public RegistrationForm setCityId(Integer cityId) {
        this.cityId = cityId;
        return this;
    }

    public Integer getOccupation() {
        return occupation;
    }

    public RegistrationForm setOccupation(Integer occupation) {
        this.occupation = occupation;
        return this;
    }

    public Integer getEnrollProBatchId() {
        return enrollProBatchId;
    }

    public RegistrationForm setEnrollProBatchId(Integer enrollProBatchId) {
        this.enrollProBatchId = enrollProBatchId;
        return this;
    }

    public Integer getCertBatchId() {
        return certBatchId;
    }

    public RegistrationForm setCertBatchId(Integer certBatchId) {
        this.certBatchId = certBatchId;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public RegistrationForm setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public RegistrationForm setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getArchiveNo() {
        return archiveNo;
    }

    public RegistrationForm setArchiveNo(String archiveNo) {
        this.archiveNo = archiveNo;
        return this;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public RegistrationForm setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
        return this;
    }

    public String getCellphone() {
        return cellphone;
    }

    public RegistrationForm setCellphone(String cellphone) {
        this.cellphone = cellphone;
        return this;
    }

    public Integer getDegree() {
        return degree;
    }

    public RegistrationForm setDegree(Integer degree) {
        this.degree = degree;
        return this;
    }

    public Integer getEduLevel() {
        return eduLevel;
    }

    public RegistrationForm setEduLevel(Integer eduLevel) {
        this.eduLevel = eduLevel;
        return this;
    }

    public String getEmail() {
        return Email;
    }

    public RegistrationForm setEmail(String email) {
        Email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegistrationForm setPassword(String password) {
        this.password = password;
        return this;
    }

    public Integer getGraduateSchool() {
        return graduateSchool;
    }

    public RegistrationForm setGraduateSchool(Integer graduateSchool) {
        this.graduateSchool = graduateSchool;
        return this;
    }

    public Integer getLearnType() {
        return learnType;
    }

    public RegistrationForm setLearnType(Integer learnType) {
        this.learnType = learnType;
        return this;
    }

    public Integer getMajor() {
        return major;
    }

    public RegistrationForm setMajor(Integer major) {
        this.major = major;
        return this;
    }

    public Integer getNormalMajor() {
        return normalMajor;
    }

    public RegistrationForm setNormalMajor(Integer normalMajor) {
        this.normalMajor = normalMajor;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public RegistrationForm setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Integer getPolitical() {
        return political;
    }

    public RegistrationForm setPolitical(Integer political) {
        this.political = political;
        return this;
    }

    public Integer getPthLevel() {
        return pthLevel;
    }

    public RegistrationForm setPthLevel(Integer pthLevel) {
        this.pthLevel = pthLevel;
        return this;
    }

    public String getResidence() {
        return residence;
    }

    public RegistrationForm setResidence(String residence) {
        this.residence = residence;
        return this;
    }

    public Integer getTechniqueJob() {
        return techniqueJob;
    }

    public RegistrationForm setTechniqueJob(Integer techniqueJob) {
        this.techniqueJob = techniqueJob;
        return this;
    }

    public String getWorkUnits() {
        return workUnits;
    }

    public RegistrationForm setWorkUnits(String workUnits) {
        this.workUnits = workUnits;
        return this;
    }

    public String getZipCole() {
        return zipCole;
    }

    public RegistrationForm setZipCole(String zipCole) {
        this.zipCole = zipCole;
        return this;
    }

    public Integer getExam() {
        return exam;
    }

    public RegistrationForm setExam(Integer exam) {
        this.exam = exam;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public RegistrationForm setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getDataFrom() {
        return dataFrom;
    }

    public RegistrationForm setDataFrom(Integer dataFrom) {
        this.dataFrom = dataFrom;
        return this;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public RegistrationForm setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
        return this;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public RegistrationForm setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
        return this;
    }
}
