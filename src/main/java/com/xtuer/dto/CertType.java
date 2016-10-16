package com.xtuer.dto;

/**
 * 资格种类 dto.CertType:
 * certType: [{id, name}, {id, name}]
 * select * from T_CERT_TYPE t
 */
public class CertType {
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
