package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 任教学科 dto.Subject:
 * subject_parentId: [{id, name}, {}]
 *
 * Created by microacup on 2016/10/17.
 */
@Getter
@Setter
public class Resume {
    private Long regId;//报名号
    private String startDate;//开始时间
    private String endDate;//结束时间
    private String workUnit;//单位
    private String job;//职务
    private String certifier;//证明人

}
