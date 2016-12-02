package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 机构撤销与合并
 *
 */
@Getter
@Setter
public class OrgAnnulLog {
    private int id;
    private int orgId;
    private String oldName;
    private Date annulDate;
}
