package com.xtuer.dto;

import java.util.Date;

/**
 * 注册历史表
 *
 * Created by microacup on 2016/10/24.
 */
public class Enrollhistory {
    private int id;
    private String certNo; //证书号码
    private String idNo; //证件号码
    private int judgmentStatus;//终审注册状态
    private Date enrollTime;//注册日期

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Integer getJudgmentStatus() {
        return judgmentStatus;
    }

    public void setJudgmentStatus(Integer judgmentStatus) {
        this.judgmentStatus = judgmentStatus;
    }

    public Date getEnrollTime() {
        return enrollTime;
    }

    public void setEnrollTime(Date enrollTime) {
        this.enrollTime = enrollTime;
    }
}
