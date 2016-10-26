package com.xtuer.dto;

/**
 * 注册正式表
 *
 * Created by microacup on 2016/10/24.
 */
public class Enrollment {
    private String certNo; //证书号码
    private String idNo; //证件号码
    private int idType; //证件类型
    private boolean inHistory;//是否在历史库中
    private int enrollNum; //注册类型
    private int province;// 省
    private int city; //市
    private int org; //机构
    private boolean inRegistration;
    private int regId;

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public boolean isInHistory() {
        return inHistory;
    }

    public void setInHistory(boolean inHistory) {
        this.inHistory = inHistory;
    }

    public int getEnrollNum() {
        return enrollNum;
    }

    public void setEnrollNum(int enrollNum) {
        this.enrollNum = enrollNum;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getOrg() {
        return org;
    }

    public void setOrg(int org) {
        this.org = org;
    }

    public boolean isInRegistration() {
        return inRegistration;
    }

    public void setInRegistration(boolean inRegistration) {
        this.inRegistration = inRegistration;
    }

    public int getRegId() {
        return regId;
    }

    public void setRegId(int regId) {
        this.regId = regId;
    }
}
