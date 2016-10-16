package com.xtuer.dto;

/**
 * 认定机构 dto.Organization: 市或省下面的所有 org
 * org_certTypeId_c_cityId: [{id, name}, {}]
 * org_certTypeId_p_provinceId
 * select * from t_org where (t.org_id = ? or t.parent_id = ?) and t.org_type = (select c.admin_level from t_cert_type c where c.ct_id = ? ) and t.status=1 order by t.area_code
 */
public class Organization {
    private int id;
    private String name;

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
}
