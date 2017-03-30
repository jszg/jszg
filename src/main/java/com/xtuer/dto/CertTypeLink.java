package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 选择市 dto.City: 省下面所有的市
 * cities__provinceId: [{id, name}, {}]
 * select * from t_org t where t.parent_id = ? and t.org_type != 1 and t.status = 1
 */
@Getter
@Setter
public class CertTypeLink {
    private int id;
    private Long certTypeId;
    private Integer eduLevelId;

}
