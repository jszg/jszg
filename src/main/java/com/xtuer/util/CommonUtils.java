package com.xtuer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {
    /**
     * 格式化日期
     *
     * @param date
     * @param format 日期格式
     * @return 日期格式化的字符串
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
