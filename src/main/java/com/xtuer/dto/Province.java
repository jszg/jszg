package com.xtuer.dto;

import java.util.Date;

/**
 * 选择省 dto.Province: 所有的省
 * provinces: [{id, name}, {}]
 * select * from t_org t where t.org_type = 4 and t.status = 1 order by t.area_code
 */
public class Province {
    private int id;
    private String name;
    private boolean provinceCity;

    private String areaName;
    private String oldName;
    private Date changeDate;

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

    public boolean getProvinceCity() {
        return provinceCity;
    }

    public void setProvinceCity(boolean provinceCity) {
        this.provinceCity = provinceCity;
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

}
