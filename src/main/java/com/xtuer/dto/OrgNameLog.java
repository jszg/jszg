package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *机构更名
 */
@Getter
@Setter
public class OrgNameLog {
    private int id;
    private int orgId;
    private String oldName;
    private Date changeDate;

}
