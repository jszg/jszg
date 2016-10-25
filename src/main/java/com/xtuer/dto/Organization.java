package com.xtuer.dto;

import java.util.Date;

/**
 * 认定机构 dto.Organization: 市或省下面的所有 org
 * org_certTypeId_c_cityId: [{id, name}, {}]
 * org_certTypeId_p_provinceId
 * select * from t_org where (t.org_id = ? or t.parent_id = ?) and t.org_type = (select c.admin_level from t_cert_type c where c.ct_id = ? ) and t.status=1 order by t.area_code
 */
public class Organization {
    private int id;
    private String name;
    private String areaName;
    private String oldName;
    private Date changeDate;
    private Date annulDate;
    private int parent;
    private int orgType;

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

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public Date getAnnulDate() {
        return annulDate;
    }

    public void setAnnulDate(Date annulDate) {
        this.annulDate = annulDate;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getOrgType() {
        return orgType;
    }

    public void setOrgType(int orgType) {
        this.orgType = orgType;
    }
}
