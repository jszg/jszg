package com.xtuer.bean;

import java.util.Date;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 这个是在保存注册信息时可能用到的字段
 *
 * Created by microacup on 2016/10/26.
 */
public class EnrollmentForm {
    @NotBlank(message="证件号码不能为空")
    private String idNo; // 证件号码

    @NotBlank(message="姓名不能为空")
    private String name;

    @NotBlank(message="证书号码不能为空")
    private String certNo; // 证书号码

    @NotNull(message="证件类型不能为空")
    private Integer idType; // 证件类型

    private Integer enrollNumber; // 注册次数

    @NotNull(message="inHistory 不能为空")
    private Boolean inHistory; // 是否在认定历史库中

    @NotNull(message="inRegistration 不能为空")
    private Boolean inRegistration; // 是否在认定表中

    @NotNull(message="最高学位不能为空")
    private Integer degree; // 最高学位

    @NotNull(message="最高学历不能为空")
    private Integer eduLevel; // 最高学历

    @NotNull(message="最高学历学习形式不能为空")
    private Integer learnType; // 最高学历学习形式

    @NotNull(message="最高学历专业类别不能为空")
    private Integer normalMajor; // 最高学历专业类别

    @NotNull(message="最高学历毕业学校不能为空")
    private Integer graduationCollege; // 最高学历毕业学校

    @NotNull(message="最高学历所学专业不能为空")
    private Integer major; // 最高学历所学专业

    @NotBlank(message="最高学历毕业时间不能为空")
    private String graduationTime; // 最高学历毕业时间

    @NotNull(message="政治面貌不能为空")
    private Integer political; // 政治面貌

    @NotNull(message="任教学校所在地不能为空")
    private Integer workUnitType; // 任教学校所在地

    @NotNull(message="现任教学校性质不能为空")
    private Integer schoolQuale; // 现任教学校性质

    @NotBlank(message="现任教学校不能为空")
    private String workUnit; // 现任教学校

    @NotBlank(message="现任教学校聘用起始日期不能为空")
    private String workDate; // 现任教学校聘用起始日期

    @NotNull(message="岗位性质不能为空")
    private Integer postQuale; // 岗位性质

    @NotNull(message="普通话水平不能为空")
    private Integer pthLevel; // 普通话水平

    @NotBlank(message="开始参加工作时间不能为空")
    private String beginWorkYear; // 开始参加工作时间

    @NotNull(message="教师职务（职称）不能为空")
    private Integer techniqueJob; // 教师职务（职称）

    @NotBlank(message="出生地不能为空")
    private String birthPlace; // 出生地

    @NotBlank(message="户籍所在地不能为空")
    private String residence; // 户籍所在地

    @NotBlank(message="通讯地址不能为空")
    private String address; // 通讯地址

    @NotBlank(message="通讯地的邮编不能为空")
    @Pattern(regexp="^[1-9][0-9]{5}$", message="请填写 6 位阿拉伯数字的邮编")
    private String zipCode; // 通讯地的邮编

    @NotBlank(message="联系电话不能为空")
    private String phone; // 联系电话

    @NotBlank(message="手机不能为空")
    @Pattern(regexp="^\\d{11}$", message="请填写 11 位阿拉伯数字的手机号码")
    private String cellphone; // 手机

    @NotBlank(message="照片不能为空")
    private String tmpPhoto; // 照片

    @NotNull(message="省不能为空")
    private Integer provinceId; // 省

//    @NotNull(message="市不能为空")
    private Integer city; // 市

    @NotNull(message="机构不能为空")
    private Integer orgId; // 机构

    @NotNull(message="现任教学段不能为空")
    private Integer teachGrade; // 现任教学段

    @NotNull(message="确认点不能为空")
    private Long localeId; // 确认点

    @NotNull(message="确认点安排不能为空")
    private Integer localSetId; // 确认点安排

    @NotNull(message="现任教学科不能为空")
    private Integer teachSubject;// 现任教学科

    @NotNull(message="注册批次不能为空")
    private Integer enrollBatch; // 注册批次

    @NotBlank(message="邮件地址不能为空")
    @Pattern(regexp="^.+@.+\\..+$", message="请填写正确的邮件地址")
    private String email; // 电子邮件

    @NotBlank(message="密码不能为空")
    @Size(min=8, message="密码不少于 8 位")
    private String password; // 密码

    private Integer confirmStatus; // 确认状态

    private Integer status; // 初审注册状态

    private Integer reCheckStatus; // 复核注册状态

    private Integer judgmentStatus; // 终审注册状态

    private String ip; // ip

    private Date lastModify;// 最后修改时间
    private String lastModifier; // 最后修改人
    private Integer deleteStatus; // 删除标记

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    public Integer getEnrollNumber() {
        return enrollNumber;
    }

    public void setEnrollNumber(Integer enrollNumber) {
        this.enrollNumber = enrollNumber;
    }

    public boolean isInHistory() {
        return inHistory;
    }

    public void setInHistory(boolean inHistory) {
        this.inHistory = inHistory;
    }

    public boolean isInRegistration() {
        return inRegistration;
    }

    public void setInRegistration(boolean inRegistration) {
        this.inRegistration = inRegistration;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Integer getEduLevel() {
        return eduLevel;
    }

    public void setEduLevel(Integer eduLevel) {
        this.eduLevel = eduLevel;
    }

    public Integer getLearnType() {
        return learnType;
    }

    public void setLearnType(Integer learnType) {
        this.learnType = learnType;
    }

    public Integer getNormalMajor() {
        return normalMajor;
    }

    public void setNormalMajor(Integer normalMajor) {
        this.normalMajor = normalMajor;
    }

    public Integer getGraduationCollege() {
        return graduationCollege;
    }

    public void setGraduationCollege(Integer graduationCollege) {
        this.graduationCollege = graduationCollege;
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public String getGraduationTime() {
        return graduationTime;
    }

    public void setGraduationTime(String graduationTime) {
        this.graduationTime = graduationTime;
    }

    public Integer getPolitical() {
        return political;
    }

    public void setPolitical(Integer political) {
        this.political = political;
    }

    public Integer getWorkUnitType() {
        return workUnitType;
    }

    public void setWorkUnitType(Integer workUnitType) {
        this.workUnitType = workUnitType;
    }

    public Integer getSchoolQuale() {
        return schoolQuale;
    }

    public void setSchoolQuale(Integer schoolQuale) {
        this.schoolQuale = schoolQuale;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public Integer getPostQuale() {
        return postQuale;
    }

    public void setPostQuale(Integer postQuale) {
        this.postQuale = postQuale;
    }

    public Integer getPthLevel() {
        return pthLevel;
    }

    public void setPthLevel(Integer pthLevel) {
        this.pthLevel = pthLevel;
    }

    public String getBeginWorkYear() {
        return beginWorkYear;
    }

    public void setBeginWorkYear(String beginWorkYear) {
        this.beginWorkYear = beginWorkYear;
    }

    public Integer getTechniqueJob() {
        return techniqueJob;
    }

    public void setTechniqueJob(Integer techniqueJob) {
        this.techniqueJob = techniqueJob;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getTmpPhoto() {
        return tmpPhoto;
    }

    public void setTmpPhoto(String tmpPhoto) {
        this.tmpPhoto = tmpPhoto;
    }

    public Integer getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(Integer confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getReCheckStatus() {
        return reCheckStatus;
    }

    public void setReCheckStatus(Integer reCheckStatus) {
        this.reCheckStatus = reCheckStatus;
    }

    public Integer getJudgmentStatus() {
        return judgmentStatus;
    }

    public void setJudgmentStatus(Integer judgmentStatus) {
        this.judgmentStatus = judgmentStatus;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getTeachGrade() {
        return teachGrade;
    }

    public void setTeachGrade(Integer teachGrade) {
        this.teachGrade = teachGrade;
    }

    public Long getLocaleId() {
        return localeId;
    }

    public void setLocaleId(Long localeId) {
        this.localeId = localeId;
    }

    public Integer getTeachSubject() {
        return teachSubject;
    }

    public void setTeachSubject(Integer teachSubject) {
        this.teachSubject = teachSubject;
    }

    public Integer getEnrollBatch() {
        return enrollBatch;
    }

    public void setEnrollBatch(Integer enrollBatch) {
        this.enrollBatch = enrollBatch;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public String getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public Integer getLocalSetId() {
        return localSetId;
    }

    public void setLocalSetId(Integer localSetId) {
        this.localSetId = localSetId;
    }
}
