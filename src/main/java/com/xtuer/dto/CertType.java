package com.xtuer.dto;

/**
 * 资格种类 dto.CertType:
 * certTypes: [{id, name}, {id, name}]
 * select * from T_CERT_TYPE t
 */
public class CertType {
    private Long id;
    private String name;
    private int code;

    public Long getId() {
        return id;
    }

    public CertType setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
