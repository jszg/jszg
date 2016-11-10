package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 认定历史库
 *
 * Created by microacup on 2016/10/24.
 */
@Getter
@Setter
public class HistoryValid {
    private int id;
    private String name;              //姓名
    private String idNo;              //证件号码
    private String idTypeName;        //证件类型
    private String certNo;            //教师资格证书号码
    private String sexName;              //性别
    private Date birthday;            //出生日期
    private String nationName;        //民族
    private String certType;     //资格种类
    private String subjectName;           //任教学科
    private String orgName;          //认定机构
    private String tmpPhoto;         //照片
    private int deleteStatus;    //数据状态
    private Date certAssign;

}

