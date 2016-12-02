package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 认定机构 dto.Organization: 市或省下面的所有 org
 * org_certTypeId_c_cityId: [{id, name}, {}]
 * org_certTypeId_p_provinceId
 * select * from t_org where (t.org_id = ? or t.parent_id = ?) and t.org_type = (select c.admin_level from t_cert_type c where c.ct_id = ? ) and t.status=1 order by t.area_code
 */
@Getter
@Setter
public class Organization {
    private Integer id;
    private String name;
    private String areaName;
    private String oldName;
    private Date changeDate;
    private Date annulDate;
    private int parent;
    private int orgType;
    private Integer provinceId;
    private String memo;

    public Organization(){

    }

    public Organization(Integer id){
        this.id = id ;
    }

}
