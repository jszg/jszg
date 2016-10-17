package com.xtuer.util;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.function.Supplier;

/**
 * RedisUtils
 *
 * Created by SUNX on 2016/10/16.
 */
public class RedisUtils {
    public static <T> T get(Class<T> clazz, StringRedisTemplate redisTemplate, String redisKey, Supplier<T> supplier) {
        T d = null;
        String json = redisTemplate.opsForValue().get(redisKey);

        if (json != null) {
            // 如果解析发生异常，有可能是 Redis 里的数据无效，故把其从 Redis 删除
            try {
                d = JSON.parseObject(json, clazz);
            } catch (Exception ex) {
                redisTemplate.delete(redisKey);
            }
        }

        if (d == null) {
            d = supplier.get();

            if (d != null) {
                redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(d));
            }
        }

        return d;
    }
}
