package com.xtuer.dto;

import java.util.Date;

/**
 * OrgBatchTime
 *
 * Created by microacup on 2016/10/25.
 */
public class OrgBatchTime {
    private Integer orgBatch;//认定机构批次
    private Date validBeginDate;//网报开始时间
    private Date validEndDate;//网报结束时间

    public Integer getOrgBatch() {
        return orgBatch;
    }

    public void setOrgBatch(Integer orgBatch) {
        this.orgBatch = orgBatch;
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
}
