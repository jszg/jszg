package com.xtuer.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by wenxl on 2017/5/8.
 * 验证身份证
 */
public class IdUtils {

    /**
     * 功能：判断字符串是否为数字
     *
     * @param content
     * @return
     */
    public static boolean isNumeric(String content) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(content);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证身份证号是否正确
     *
     * @param idNo
     * @return
     */
    public static boolean isIDCard(String idNo) {
        if (idNo == null || idNo.length() != 18) {
            return false;
        }
        // 验证前17位是否为数字
        if (!isNumeric(idNo.substring(0, 17))) {
            return false;
        }
		/* 出生年月是否有效 */
        try {
            // 年份
            int year = Integer.parseInt(idNo.substring(6, 10));
            // 月份
            int month = Integer.parseInt(idNo.substring(10, 12));
            // 日
            int day = Integer.parseInt(idNo.substring(12, 14));
            GregorianCalendar calendar = new GregorianCalendar();

            int currentYear = calendar.get(Calendar.YEAR);
            if (currentYear - year > 80 || currentYear - year < 15) {
                return false;
            }
            if (month > 12 || month == 0) {
                return false;
            }
            if (day > 31 || day == 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
		/* 地区码是否有效 */
        Hashtable<String, String> areaCodes = getAreaCode();
        if (areaCodes.get(idNo.substring(0, 2)) == null) {
            return false;
        }
        return true;
    }

    /**
     * 获得地区编码
     *
     * @return
     */
    private static Hashtable<String, String> getAreaCode() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }
}
