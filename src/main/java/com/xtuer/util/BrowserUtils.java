package com.xtuer.util;

import javax.servlet.http.HttpServletRequest;

public class BrowserUtils {
    public static final String IE11    = "rv:11";
    public static final String IE9     = "MSIE 9.0";
    public static final String IE8     = "MSIE 8.0";
    public static final String IE7     = "MSIE 7.0";
    public static final String IE6     = "MSIE 6.0";
    public static final String MAXTHON = "Maxthon";
    public static final String QQ      = "QQBrowser";
    public static final String GREEN   = "GreenBrowser";
    public static final String SE360   = "360SE";
    public static final String FIREFOX = "Firefox";
    public static final String OPERA   = "OPR";
    public static final String CHROME  = "Chrome";
    public static final String SAFARI  = "Safari";
    public static final String OTHER   = "other";

    /**
     * 取得浏览器的名字
     *
     * @param request
     * @return 浏览器的名字
     */
    public static String getBrowserName(HttpServletRequest request) {
        return getBrowserName(request.getHeader("user-Agent"));
    }

    public static String getBrowserName(String userAgent) {
        if (userAgent == null) {
            return OTHER;
        }

        if (userAgent.indexOf(FIREFOX) != -1) {
            return FIREFOX;
        }

        if (userAgent.indexOf(IE11) != -1) {
            return "IE11";
        }

        if (userAgent.indexOf(IE9) != -1) {
            return IE9;
        }

        if (userAgent.indexOf(IE8) != -1) {
            return IE8;
        }

        if (userAgent.indexOf(IE7) != -1) {
            return IE7;
        }

        if (userAgent.indexOf(IE6) != -1) {
            return IE6;
        }

        if (userAgent.indexOf(SE360) != -1) {
            return SE360;
        }

        if (userAgent.indexOf(OPERA) != -1) {
            return OPERA;
        }

        if (userAgent.indexOf(SAFARI) != -1) {
            if (userAgent.indexOf(CHROME) != -1) {
                return CHROME;
            }

            return SAFARI;
        }

        if (userAgent.indexOf(CHROME) != -1) {
            return CHROME;
        }

        if (userAgent.indexOf(MAXTHON) != -1) {
            return MAXTHON;
        }

        if (userAgent.indexOf(QQ) != -1) {
            return QQ;
        }

        if (userAgent.indexOf(GREEN) != -1) {
            return GREEN;
        }

        return OTHER;
    }

    public static String getBrowserContent(HttpServletRequest request) {
        String content = "";

        if(request.getHeader("User-Agent") != null) {
            if (request.getHeader("User-Agent").length() > 256) {
                content = request.getHeader("User-Agent").substring(256);
            } else {
                content = request.getHeader("User-Agent");
            }
        }

        return content;
    }
}
