package com.xtuer.dto;

public class CityInfo {
    private int orgType;
    private int parentId;
    private int parentOrgType;
    private boolean provinceCity;

    public int getOrgType() {
        return orgType;
    }

    public void setOrgType(int orgType) {
        this.orgType = orgType;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getParentOrgType() {
        return parentOrgType;
    }

    public void setParentOrgType(int parentOrgType) {
        this.parentOrgType = parentOrgType;
    }

    public boolean isProvinceCity() {
        return provinceCity;
    }

    public void setProvinceCity(boolean provinceCity) {
        this.provinceCity = provinceCity;
    }
}
