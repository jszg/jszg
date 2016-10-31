package com.xtuer.service;

import com.xtuer.bean.Result;
import com.xtuer.constant.RedisKey;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisAclService {
    private static Logger logger = LoggerFactory.getLogger(RedisAclService.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource(name = "config")
    private PropertiesConfiguration config;

    public long currentCount() {
        return redisTemplate.opsForZSet().size(RedisKey.ACL_KEY);
    }

    public long maxCount() {
        return config.getInteger("aclCount", 2000);
    }

    public boolean inIpList(String ip) {
        return redisTemplate.opsForZSet().rank(RedisKey.ACL_KEY, ip) != null;
    }

    public void addToIpList(String ip) {
        redisTemplate.opsForZSet().add(RedisKey.ACL_KEY, ip, System.currentTimeMillis());
    }

    public void reset() {
        redisTemplate.delete(RedisKey.ACL_KEY);
    }

    public Result setUpMaxCount(String password, int maxCount) {
        if (password == null || !password.equals(config.getString("aclPassword"))) {
            return new Result(false, "请输入正确的密码");
        }

        maxCount = maxCount > 0 ? maxCount : 2000;
        config.setProperty("aclCount", maxCount);
        logger.debug("访问人数限制到 {}", maxCount());

        return Result.ok();
    }
}
