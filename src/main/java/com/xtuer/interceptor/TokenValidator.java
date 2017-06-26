package com.xtuer.interceptor;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证注册认定的表单提交防止表单重复提交
 */
public class TokenValidator implements HandlerInterceptor {

    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!"GET".equalsIgnoreCase(request.getMethod())){
            String clientToken = request.getParameter("token");
            if(StringUtils.isEmpty(clientToken) || clientToken.isEmpty()){
                throw new RuntimeException("信息提交失败，请按照报名流程重新填报或者更换其他浏览器(比如谷歌或者火狐)");
            }
            String serverToken = redisTemplate.opsForValue().get(clientToken);
            if(StringUtils.isEmpty(serverToken) || !clientToken.trim().equalsIgnoreCase(serverToken.trim())){
                throw new RuntimeException("信息提交失败，请按照报名流程重新填报或者更换其他浏览器(比如谷歌或者火狐)");
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(!"GET".equalsIgnoreCase(request.getMethod())){
            return;
        }
        String token = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        modelAndView.addObject("token", token);
        redisTemplate.opsForValue().set(token, token,3600, TimeUnit.SECONDS);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    public StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

}
