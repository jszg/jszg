package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 注册正式表
 *
 * Created by microacup on 2016/10/24.
 */
@Setter
@Getter
public class Enrollment {
    private String certNo; //证书号码
    private String idNo; //证件号码
    private int idType; //证件类型
    private boolean inHistory;//是否在历史库中
    private int enrollNum; //注册类型
    private int province;// 省
    private int city; //市
    private int org; //机构
    private boolean inRegistration;
    private int regId;
}
