package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 确认点
 *
 * Created by microacup on 2016/10/24.
 */
@Setter
@Getter
public class LocalSet {
    private int id;
    private int orgId;
    private String name;
    private Date beginDate;
    private Date endDate;
    private String confirmRange;
    private Date validBeginDate;
    private Date validEndDate;
}
