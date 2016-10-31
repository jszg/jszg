package com.xtuer.scheduler;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;

public class RedisAclScheduler {
    public void execute() {
        logger.debug("清除超时的 IP at {}", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

    private static Logger logger = LoggerFactory.getLogger(RedisAclScheduler.class);

    @Autowired
    private StringRedisTemplate redisTemplate;
}
