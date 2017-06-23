package com.xtuer.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * Created by wenxl on 2017/6/21.
 */
public class NetUtils {

    private static Logger logger = LoggerFactory.getLogger(NetUtils.class);
    /**
     * 判断请求是否 AJAX 请求
     *
     * @param request HttpServletRequest 对象
     * @return 如果是 AJAX 请求则返回 true，否则返回 false
     */
    public static boolean useAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }
    /**
     * 使用 AJAX 的方式把响应写入 response 中
     *
     * @param response HttpServletResponse 对象，用于写入请求的响应
     * @param data 响应的数据
     */
    public static void ajaxResponse(HttpServletResponse response, String data) {
        response.setContentType("application/json"); // 使用 ajax 的方式
        response.setCharacterEncoding("UTF-8");
        try {
            // 写入数据到流里，刷新并关闭流
            PrintWriter writer = response.getWriter();
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            logger.warn(ExceptionUtils.getStackTrace(ex));
        }
    }

}
