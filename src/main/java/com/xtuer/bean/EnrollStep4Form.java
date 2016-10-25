package com.xtuer.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Step4
 *
 * Created by microacup on 2016/10/25.
 */
public class EnrollStep4Form {

    // 请按教师资格证书上的内容如实填写下列信息项
    @JSONField(format="yyyy-MM-dd")
    public Date certAssignDate;
    public int certType;
    public int requestOrg; // 认定机构
    public int registerSubject;
    public String name;
    public int sex;
    public String birth;
    public int nation;

    // 请按现阶段工作情况选择下列信息
    public int teachGrade;
    public int province;
    public int city;
    public int registerOrg;
    public int teachSubject;

    public String proCode;

}
