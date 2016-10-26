package com.xtuer.dto;

import java.util.Date;

/**
 * 确认点
 *
 * Created by microacup on 2016/10/24.
 */
public class LocalSet {
    private int id;
    private int orgId;
    private String name;
    private Date beginDate;
    private Date endDate;
    private String confirmRange;
    private Date validBeginDate;
    private Date validEndDate;

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


    public String getConfirmRange() {
        return confirmRange;
    }

    public void setConfirmRange(String confirmRange) {
        this.confirmRange = confirmRange;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getValidBeginDate() {
        return validBeginDate;
    }

    public void setValidBeginDate(Date validBeginDate) {
        this.validBeginDate = validBeginDate;
    }

    public Date getValidEndDate() {
        return validEndDate;
    }

    public void setValidEndDate(Date validEndDate) {
        this.validEndDate = validEndDate;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }
}
