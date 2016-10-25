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
    private int sexName;              //性别
    private Date birthday;            //出生日期
    private int nationName;        //民族
    private int certType;      //资格种类
    private int subjectName;           //任教学科
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

    public int getSexName() {
        return sexName;
    }

    public void setSexName(int sexName) {
        this.sexName = sexName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getNationName() {
        return nationName;
    }

    public void setNationName(int nationName) {
        this.nationName = nationName;
    }

    public int getCertType() {
        return certType;
    }

    public void setCertType(int certType) {
        this.certType = certType;
    }

    public int getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(int subjectName) {
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
