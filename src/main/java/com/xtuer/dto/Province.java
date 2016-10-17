package com.xtuer.dto;

/**
 * 选择省 dto.Province: 所有的省
 * provinces: [{id, name}, {}]
 * select * from t_org t where t.org_type = 4 and t.status = 1 order by t.area_code
 */
public class Province {
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
