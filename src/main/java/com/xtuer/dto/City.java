package com.xtuer.dto;

/**
 * 选择市 dto.City: 省下面所有的市
 * cities__provinceId: [{id, name}, {}]
 * select * from t_org t where t.parent_id = ? and t.org_type != 1 and t.status = 1
 */
public class City {
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
