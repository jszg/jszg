package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 限制库
 *
 * Created by microacup on 2016/10/24.
 */
@Getter
@Setter
public class Limitation {
    private int id;
    private String certNo;
    private String idNo;
    private int status;
    private Integer type;
    private Date dueTime;
    private String reason;

}
