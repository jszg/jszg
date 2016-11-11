package com.xtuer.bean;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * 这是认定需要用到的字段
 *
 * Created by microacup on 2016/10/26.
 */

@Getter
@Setter
public class RegistrationForm {
    private long regId;

    private String  address;//地址
    private String  archiveno;//档案号
    private String  birthday;//出生日期
    private Date    birthdayDate;

    private String  birthPlace;//出生地
    private Integer certBatchId;//批次
    private String  cellPhone;//手机
    private String  certAssign;//证书签发日期
    private Date    certAssignDate;

    private String  certNo;//证书号码
    private Integer cityId;//市
    private Integer certType;//资格种类
    private Integer dataFrom;//数据来源
    private Integer degreeId;//最高学位
    private Integer deleteStatus;//删除状态
    private Integer eduLevelId;;//最高学历
    private String  email;//电子邮件
    private Integer enrollProBatchId;//注册批次
    private Integer exam;//考试类型
    private Integer graduateSchool;//毕业学校
    private String  graduateShoolName;//毕业学校名称
    private Integer graduateId;//是否在校生
    private String  graduaTime;//毕业时间
    private Date    graduaTimeDate;

    private  String idNo;//证件号码
    private Integer idType;//证件类型
    private Integer learnType;//学习形式
    private Integer majorId;//所学专业
    private String  name;//姓名
    private Integer nation;//民族
    private Integer normalMajor;//专业类别
    private Integer occupation;//现从事职业
    private Integer orgId;//机构id
    private String  orgName;//机构名称
    private Integer localeSet;//确认点安排
    private Integer localeId;//确认机构
    private String  phone;//手机
    private Integer political;//政治面貌
    private Integer provinceId;//省
    private Integer pthevelId;//普通话水平
    private String  residence;//户籍所在地
    private Integer sex;//性别
    private Integer status;//认定状态
    private String  workUnits;//工作单位
    private String  zipCode;//邮编
    private Integer subjectId;//任教学科
    private String  password;//密码
    private Integer techniqueJobId;//教师职务
    private Date    applyTime;//申请日期
    private Date    lastModify;//最后修改日期
    private String  lastModifier;//最后修改人
    private Date    triggerTime;//触发时间
    private String  pthCertNo;//普通话证书
    private String  pthOrg;//普通话发证单位
    @NotBlank(message="照片不能为空")
    private String  tmpPhoto; // 照片
    private String  ip;//ip
    private String  statusMemo;//备注,用于区分新旧系统报名数据
    private String  resumInfo;//简历信息

}
