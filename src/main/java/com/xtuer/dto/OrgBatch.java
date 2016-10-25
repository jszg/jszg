package com.xtuer.dto;

/**
 * OrgBatch
 *
 * Created by microacup on 2016/10/25.
 */
public class OrgBatch {
    private boolean joinIn;//是否参加本次计划
    private boolean isOpen;//是否开通网报
    private int provinceBatch;//所属省级计划

    public boolean isJoinIn() {
        return joinIn;
    }

    public void setJoinIn(boolean joinIn) {
        this.joinIn = joinIn;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getProvinceBatch() {
        return provinceBatch;
    }

    public void setProvinceBatch(int provinceBatch) {
        this.provinceBatch = provinceBatch;
    }
}
