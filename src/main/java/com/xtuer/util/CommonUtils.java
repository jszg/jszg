package com.xtuer.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CommonUtils {

    /**
     * 验证密码的强度
     *     密码不少于 8 位，必须包含数字、字母和特殊字符，特殊字符需从 “#、%、*、-、_、!、@、$、&” 中选择
     * @param password 密码
     * @return 如果密码的强度够返回 true，否则返回 false
     */
    public static boolean passwordHasEnoughStrength(String password) {
        return  password.length() >= 8 &&
                password.matches("^.*[0-9]+.*$") &&
                password.matches("^.*[a-zA-Z]+.*$") &&
                password.matches("^.*[#%_!@&\\-\\*\\$]+.*$");
    }

    /**
     * 生成一个 UUID
     * @return UUID 字符串
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 使用 UUID 生成不会重复的文件名, 后缀和原来的后缀一样(为了统一, 使用小写)
     * @param fileName 文件名
     * @return 不会重复的文件名
     */
    public static String generateUniqueFileName(String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        extension = StringUtils.isEmpty(extension) ? "" : "." + extension.toLowerCase();

        return CommonUtils.generateUuid() + extension;
    }

    /**
     * 对 text 进行 MD5 编码
     * @param text
     * @return MD5 字符串
     */
    public static String md5(String text) {
        return DigestUtils.md5DigestAsHex(text.getBytes());
    }

    /**
     * 获取客户端的 IP
     * @param request
     * @return 客户端的 IP
     */
    public static String getClientIp(HttpServletRequest request) {
        final String UNKNOWN = "unknown";

        // 需要注意有多个 Proxy 的情况: X-Forwarded-For: client, proxy1, proxy2
        // 没有最近的 Proxy 的 IP, 只有 1 个 Proxy 时是客户端的 IP
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr(); // 没有使用 Proxy 时是客户端的 IP, 使用 Proxy 时是最近的 Proxy 的 IP
        }

        return ip;
    }

    public static final int getCertYearFromRegistration(String certNo,String certAssign) {
        int year = 0;
        if (certNo.length() == 15 || certNo.length() == 17) {
            if (certNo.length() > 15) {
                year = Integer.parseInt(certNo.substring(0, 4));
            }
            year = 1900 + Integer.parseInt(certNo.substring(0, 2));
        } else {
            SimpleDateFormat sdf  =   new  SimpleDateFormat( " yyyy-MM-dd" );
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(certAssign));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            year = cal.get(Calendar.YEAR);
            if (cal.get(Calendar.MONTH) <= Calendar.JUNE) {
                year--;
            }
        }
        return year;
    }
}
