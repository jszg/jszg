package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * OrgBatch
 *
 * Created by microacup on 2016/10/25.
 */
@Getter
@Setter
public class OrgBatch {
    private Boolean joinIn; // 是否参加本次计划
    private Boolean isOpen; // 是否开通网报
    private Integer provinceBatch; // 所属省级计划
    private Integer status; // 所属省级计划
    private Integer orgBatchId;  // 机构计划 id
    private Integer certBatchId; // 批次 id
    private Date beginDate;//开始时间
    private Date endDate;//结束时间

}
