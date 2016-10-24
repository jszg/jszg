package com.xtuer.dto;

import java.util.Date;

/**
 * 认定历史库
 *
 * Created by microacup on 2016/10/24.
 */
public class HistoryValid {
    private int id;
    private String name;              //姓名
    private String idNo;              //证件号码
    private int idTypeName;        //证件类型
    private String certNo;            //教师资格证书号码
    private int sexName;              //性别
    private Date birthday;            //出生日期
    private int nationName;        //民族
    private int certTypeName;     //资格种类
    private int subject;           //任教学科
    private String orgName;          //认定机构
    private String tmpPhoto;         //照片
    private boolean deleteStatus;    //数据状态

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }


    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public int getIdTypeName() {
        return idTypeName;
    }

    public void setIdTypeName(int idTypeName) {
        this.idTypeName = idTypeName;
    }

    public int getSexName() {
        return sexName;
    }

    public void setSexName(int sexName) {
        this.sexName = sexName;
    }

    public int getNationName() {
        return nationName;
    }

    public void setNationName(int nationName) {
        this.nationName = nationName;
    }

    public int getCertTypeName() {
        return certTypeName;
    }

    public void setCertTypeName(int certTypeName) {
        this.certTypeName = certTypeName;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public boolean isDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }
}
