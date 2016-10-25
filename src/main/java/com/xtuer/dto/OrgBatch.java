package com.xtuer.dto;

/**
 * OrgBatch
 *
 * Created by microacup on 2016/10/25.
 */
public class OrgBatch {
    private Boolean joinIn;//是否参加本次计划
    private Boolean isOpen;//是否开通网报
    private Integer provinceBatch;//所属省级计划

    public Boolean getJoinIn() {
        return joinIn;
    }

    public void setJoinIn(Boolean joinIn) {
        this.joinIn = joinIn;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public Integer getProvinceBatch() {
        return provinceBatch;
    }

    public void setProvinceBatch(Integer provinceBatch) {
        this.provinceBatch = provinceBatch;
    }
}
