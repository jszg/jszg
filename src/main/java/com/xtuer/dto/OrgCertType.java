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
public class OrgCertType {
    private Integer orgId;
    private Integer certTypeId;


}
