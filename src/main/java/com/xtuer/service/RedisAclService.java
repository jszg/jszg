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

    public boolean canAccess(String ip) {
        // [1] 在 IP 列表中则继续
        if (inIpList(ip)) {
            logger.debug("Allowed, already in: " + ip);
            return true;
        }

        // [2] 不在 IP 列表，但是当前使用人数小于 maxCount，则加入，继续
        if (currentCount() < maxCount()) {
            logger.debug("Allowed, new added: " + ip);
            addToIpList(ip);
            return true;
        }

        // [3] 既不在 IP 列表，同时 IP 列表超过了 maxCount，则不许访问
        return false;
    }

    public long currentCount() {
        return redisTemplate.opsForZSet().size(RedisKey.ACL_KEY);
    }

    public long maxCount() {
        return config.getInteger("aclCount", 2000);
    }

    /**
     * 每个用户允许的最大停留时间，单位为分钟
     * @return 分钟
     */
    public long maxDuration() {
        return config.getInteger("aclDuration", 20);
    }

    /**
     * 判断 IP 是否在访问列表里
     * @param ip
     * @return
     */
    public boolean inIpList(String ip) {
        return redisTemplate.opsForZSet().rank(RedisKey.ACL_KEY, ip) != null;
    }

    /**
     * 把 IP 添加到访问列表
     * @param ip
     */
    public void addToIpList(String ip) {
        redisTemplate.opsForZSet().add(RedisKey.ACL_KEY, ip, System.currentTimeMillis());
    }

    /**
     * 把 IP 从访问列表里删除
     * @param ip
     */
    public void removeFromIpList(String ip) {
        redisTemplate.opsForZSet().remove(RedisKey.ACL_KEY, ip);
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

    public Result setUpMaxDuration(String password, int maxDuration) {
        if (password == null || !password.equals(config.getString("aclPassword"))) {
            return new Result(false, "请输入正确的密码");
        }

        maxDuration = maxDuration > 0 ? maxDuration : 20;
        config.setProperty("aclDuration", maxDuration);
        logger.debug("修改每人最大停留时间为 {}", maxDuration());

        return Result.ok();
    }

    /**
     * 从 IP 列表中删除停留时间超过最大运训停留时间的 IP
     */
    public void removeAllIpsStayTooLong() {
        long max = System.currentTimeMillis() - maxDuration() * 60 * 1000; // 当前时间减去最大停留时间
        redisTemplate.opsForZSet().removeRangeByScore(RedisKey.ACL_KEY, 0, max);
    }
}
