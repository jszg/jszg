package com.xtuer.interceptor;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 验证注册认定的表单提交防止表单重复提交
 */
public class TokenValidator implements HandlerInterceptor {

    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!"GET".equalsIgnoreCase(request.getMethod())){
            String clientToken = request.getParameter("token");
            String serverToken = redisTemplate.opsForValue().get(clientToken);
            if(clientToken == null || clientToken.isEmpty() || !clientToken.equals(serverToken)){
                throw new RuntimeException("重复提交表单");
            }
            redisTemplate.delete(clientToken);
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
        redisTemplate.opsForValue().set(token, token);
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
