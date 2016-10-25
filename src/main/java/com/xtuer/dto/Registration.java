package com.xtuer.dto;

import java.util.Date;

/**
 * 认定正式表
 *
 * Created by microacup on 2016/10/24.
 */
public class Registration {
    private String name;              //姓名
    private String idNo;              //证件号码
    private String idTypeName;        //证件类型
    private String certNo;            //教师资格证书号码
    private String sexName;              //性别
    private Date birthday;            //出生日期
    private String nationName;        //民族
    private String certType;      //资格种类
    private String subjectName;           //任教学科
    private String orgName;          //认定机构
    private String tmpPhoto;         //照片

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdTypeName() {
        return idTypeName;
    }

    public void setIdTypeName(String idTypeName) {
        this.idTypeName = idTypeName;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTmpPhoto() {
        return tmpPhoto;
    }

    public void setTmpPhoto(String tmpPhoto) {
        this.tmpPhoto = tmpPhoto;
    }
}
