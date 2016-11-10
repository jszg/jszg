package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 资格种类 dto.CertType:
 * certTypes: [{id, name}, {id, name}]
 * select * from T_CERT_TYPE t
 */
@Getter
@Setter
public class CertType {
    private Long id;
    private String name;
    private int code;
    private int adminLevel;
    private int teachGrade;

    public CertType setId(Long id) {
        this.id = id;
        return this;
    }

}
