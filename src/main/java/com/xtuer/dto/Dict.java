package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典
 *
 * Created by microacup on 2016/10/18.
 */
@Setter
@Getter
public class Dict {
    private int id;
    private String name;
    private boolean status = true;
    private String code;
}
