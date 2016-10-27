package com.xtuer.bean;

import java.util.Date;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 这个是在保存注册信息时可能用到的字段
 *
 * Created by microacup on 2016/10/26.
 */
@Getter
@Setter
public class EnrollmentForm {
    private java.math.BigDecimal enrollId;

    @NotBlank(message="姓名不能为空")
    private String name;

    @NotBlank(message="证件号码不能为空")
    private String idNo; // 证件号码

    @NotBlank(message="证书号码不能为空")
    private String certNo; // 证书号码

    @NotBlank(message="证书签发日期不能为空")
    private String certAssignDate; // 证书签发日期
    private Date certAssignDateDate;

    @NotNull(message="证件类型不能为空")
    private Integer idTypeId; // 证件类型

    @NotNull(message="注册次数不能为空")
    private Integer enrollNumber; // 注册次数

    @NotNull(message="inHistory 不能为空")
    private Boolean inHistory; // 是否在认定历史库中

    @NotNull(message="inRegistration 不能为空")
    private Boolean inRegistration; // 是否在认定表中

    @NotNull(message="民族不能为空")
    private Integer nationId; // 民族的 id

    @NotNull(message="性别的 id 不能为空")
    private Integer genderId; // 性别的 id

    @NotNull(message="最高学位不能为空")
    private Integer degreeId; // 最高学位

    @NotNull(message="最高学历不能为空")
    private Integer eduLevelId; // 最高学历

    @NotNull(message="最高学历学习形式不能为空")
    private Integer learnTypeId; // 最高学历学习形式

    @NotNull(message="最高学历专业类别不能为空")
    private Integer normalMajorId; // 最高学历专业类别

    @NotNull(message="最高学历毕业学校不能为空")
    private Integer graduationCollegeId; // 最高学历毕业学校的 id

    @NotBlank(message="最高学历毕业学校名字不能为空")
    private String graduationCollegeName; // 最高学历毕业学校名字

    @NotNull(message="最高学历所学专业不能为空")
    private Integer majorId; // 最高学历所学专业

    @NotBlank(message="最高学历毕业时间不能为空")
    private String graduationTime; // 最高学历毕业时间
    private Date graduationTimeDate;

    @NotNull(message="政治面貌不能为空")
    private Integer politicalId; // 政治面貌

    @NotNull(message="任教学校所在地不能为空")
    private Integer workUnitTypeId; // 任教学校所在地

    @NotNull(message="现任教学校性质不能为空")
    private Integer schoolQualeId; // 现任教学校性质

    @NotBlank(message="现任教学校不能为空")
    private String workUnit; // 现任教学校

    @NotBlank(message="现任教学校聘用起始日期不能为空")
    private String workDate; // 现任教学校聘用起始日期
    private Date workDateDate;

    @NotBlank(message="出生日期不能为空")
    private String birthday; // 出生日期
    private Date birthdayDate;

    @NotNull(message="岗位性质不能为空")
    private Integer postQualeId; // 岗位性质

    @NotNull(message="普通话水平不能为空")
    private Integer pthLevelId; // 普通话水平

    @NotBlank(message="开始参加工作时间不能为空")
    private String beginWorkYear; // 开始参加工作时间
    private Date beginWorkYearDate;

    private int beginWorkYearInt; // 开始参加工作时间的年

    @NotNull(message="教师职务（职称）不能为空")
    private Integer technicalJobId; // 教师职务（职称）

    @NotBlank(message="出生地不能为空")
    private String birthPlace; // 出生地

    @NotBlank(message="户籍所在地不能为空")
    private String residence; // 户籍所在地

    @NotBlank(message="通讯地址不能为空")
    private String address; // 通讯地址

    @NotBlank(message="通讯地的邮编不能为空")
    @Pattern(regexp="^[1-9][0-9]{5}$", message="请填写 6 位阿拉伯数字的邮编")
    private String zipCode; // 通讯地的邮编

    @NotBlank(message="联系电话不能为空")
    private String phone; // 联系电话

    @NotBlank(message="手机不能为空")
    @Pattern(regexp="^\\d{11}$", message="请填写 11 位阿拉伯数字的手机号码")
    private String cellphone; // 手机

    @NotBlank(message="照片不能为空")
    private String tmpPhoto; // 照片

    @NotNull(message="省不能为空")
    private Integer provinceId; // 省

    private Integer cityId; // 市, 后台查询的

    @NotNull(message="机构不能为空")
    private Integer orgId; // 机构

    @NotNull(message="现任教学段不能为空")
    private Integer teachGradeId; // 现任教学段

    @NotNull(message="确认点不能为空")
    private Long localeId; // 确认点

    @NotNull(message="确认点安排不能为空")
    private Integer localSetId; // 确认点安排

    @NotNull(message="资格种类的 id 不能为空")
    private Integer certTypeId; // 资格种类的 id

    @NotNull(message="注册批次的 id 不能为空")
    private Integer certBatchId; // 注册批次

    @NotNull(message="现任教学科不能为空")
    private Integer teachSubjectId;// 现任教学科

    @NotNull(message="认定的 id 不能为空")
    private Integer registerId; // 认定的 id

    @NotNull(message="证书上的任教学科 id 不能为空")
    private Integer registerSubjectId; // 证书上的任教学科 id

    @NotNull(message="证书上的认定机构 id 不能为空")
    private Integer recognizeOrgId; // 证书上的认定机构

    @NotBlank(message="证书上的认定机构名称不能为空")
    private String recognizeOrgName; // 证书上的认定机构名称

    private Integer enrollBatch; // 注册批次，后台查询

    @NotBlank(message="邮件地址不能为空")
    @Pattern(regexp="^.+@.+\\..+$", message="请填写正确的邮件地址")
    private String email; // 电子邮件

    @NotBlank(message="密码不能为空")
    @Size(min=8, message="密码不少于 8 位")
    private String password; // 密码

    private Integer subjectId; // 任教学科，后台取

    private Integer confirmStatus; // 确认状态

    private Integer status; // 初审注册状态

    private Integer reCheckStatus; // 复核注册状态

    private Integer judgmentStatus; // 终审注册状态

    private String ip; // ip

    private Date applyTime; // 申请时间
    private Integer deleteStatus; // 删除标记
    private Integer dataFrom; // 数据来源
}
