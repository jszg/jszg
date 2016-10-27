package com.xtuer.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Log
 * <p>
 * Created by microacup on 2016/10/26.
 */
@Setter
@Getter
public class UserPortalLog {
    public static final int BACKEND_USER   = 1;
    public static final int RENDING_APPLY  = 2;
    public static final int ENROLL_APPLY   = 3;
    public static final int RENDING_SUBMIT = 4;
    public static final int ENROLL_SUBMIT  = 5;

    private int id;
    private String userId;         // 用户名
    private String browserName;    // 浏览器
    private String browserContent; // 浏览器内容
    private Integer type; // 1 后台管理用户，2 认定申请，3 注册申请
    private String ip;    // 登陆 IP
    private Date login;   // 提交时间
    private Date begin;   // 开始时间
    private Date end;     // 结束时间
}
