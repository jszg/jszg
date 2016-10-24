package com.xtuer.dto;

/**
 * 注册正式表
 *
 * Created by microacup on 2016/10/24.
 */
public class Enrollment {
    private String certNo; //证书号码
    private String idNo; //证件号码
    private long idType; //证件类型
    private int inHistory;//是否在历史库中
    private long enrollNum; //注册类型
    private long province;// 省
    private long city; //市
    private long org; //机构

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

    public long getIdType() {
        return idType;
    }

    public void setIdType(long idType) {
        this.idType = idType;
    }

    public int getInHistory() {
        return inHistory;
    }

    public void setInHistory(int inHistory) {
        this.inHistory = inHistory;
    }

    public long getEnrollNum() {
        return enrollNum;
    }

    public void setEnrollNum(long enrollNum) {
        this.enrollNum = enrollNum;
    }

    public long getProvince() {
        return province;
    }

    public void setProvince(long province) {
        this.province = province;
    }

    public long getCity() {
        return city;
    }

    public void setCity(long city) {
        this.city = city;
    }

    public long getOrg() {
        return org;
    }

    public void setOrg(long org) {
        this.org = org;
    }
}
