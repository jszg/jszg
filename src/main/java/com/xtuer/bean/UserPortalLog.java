package com.xtuer.bean;

import java.util.Date;

/**
 * Log
 *
 * Created by microacup on 2016/10/26.
 */
public class UserPortalLog {

    public static final int user=1;
    public static final int renDing=2;
    public static final int enroll=3;
    public static final int renDingSubmit=4;
    public static final int enrollSubmit=5;

    private int id;
    private String userId;//用户名
    private String browserInfo;//浏览器
    private String browserContent;//浏览器内容
    private Integer type;//1后台管理用户，2认定申请，3注册申请
    private String ip;//登陆ip
    private Date login;//提交时间
    private Date begin;//开始时间
    private Date end; //结束时间

    public UserPortalLog() {
    }

    public int getId() {
        return id;
    }

    public UserPortalLog setId(int id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public UserPortalLog setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getBrowserInfo() {
        return browserInfo;
    }

    public static String getBrowInfo(String userAgent){
        String IE11="rv:11";
        String IE9="MSIE 9.0";
        String IE8="MSIE 8.0";
        String IE7="MSIE 7.0";
        String IE6="MSIE 6.0";
        String MAXTHON="Maxthon";
        String QQ="QQBrowser";
        String GREEN="GreenBrowser";
        String SE360="360SE";
        String FIREFOX="Firefox";
        String OPERA="OPR";
        String CHROME="Chrome";
        String SAFARI="Safari";
        String OTHER="other";
        if(userAgent != null && userAgent.indexOf(FIREFOX) != -1){
            return FIREFOX;
        }
        if(userAgent != null && userAgent.indexOf(IE11) != -1){
            return "IE11";
        }
        if(userAgent != null && userAgent.indexOf(IE9) != -1){
            return IE9;
        }
        if(userAgent != null && userAgent.indexOf(IE8) != -1){
            return IE8;
        }
        if(userAgent != null && userAgent.indexOf(IE7) != -1){
            return IE7;
        }
        if(userAgent != null && userAgent.indexOf(IE6) != -1){
            return IE6;
        }
        if(userAgent != null && userAgent.indexOf(SE360) != -1){
            return SE360;
        }
        if(userAgent != null && userAgent.indexOf(OPERA) != -1){
            return OPERA;
        }
        if(userAgent != null && userAgent.indexOf(SAFARI) != -1){
            if(userAgent != null && userAgent.indexOf(CHROME) != -1){
                return CHROME;
            }
            return SAFARI;
        }
        if(userAgent != null && userAgent.indexOf(CHROME) != -1){
            return CHROME;
        }
        if(userAgent != null && userAgent.indexOf(MAXTHON) != -1){
            return MAXTHON;
        }
        if(userAgent != null && userAgent.indexOf(QQ) != -1){
            return QQ;
        }
        if(userAgent != null && userAgent.indexOf(GREEN) != -1){
            return GREEN;
        }
        return OTHER;
    }

    public UserPortalLog setBrowserInfo(String browserInfo) {
        this.browserInfo = browserInfo;
        return this;
    }

    public String getBrowserContent() {
        return browserContent;
    }

    public UserPortalLog setBrowserContent(String browserContent) {
        this.browserContent = browserContent;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public UserPortalLog setType(Integer type) {
        this.type = type;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public UserPortalLog setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Date getLogin() {
        return login;
    }

    public UserPortalLog setLogin(Date login) {
        this.login = login;
        return this;
    }

    public Date getBegin() {
        return begin;
    }

    public UserPortalLog setBegin(Date begin) {
        this.begin = begin;
        return this;
    }

    public Date getEnd() {
        return end;
    }

    public UserPortalLog setEnd(Date end) {
        this.end = end;
        return this;
    }
}
