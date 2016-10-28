package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 专业技术职务
 *
 * Created by microacup on 2016/10/19.
 */
@Getter
@Setter
public class TechnicalJob {
    private int id;
    private String name;
    private String code;
    private Integer parentId;

}
