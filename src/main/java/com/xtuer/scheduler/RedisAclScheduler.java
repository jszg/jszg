package com.xtuer.scheduler;

import com.xtuer.service.RedisAclService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class RedisAclScheduler {
    public void execute() {
        logger.debug("清除超时的 IP at {}", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        aclService.removeAllIpsStayTooLong();
    }

    private static Logger logger = LoggerFactory.getLogger(RedisAclScheduler.class);

    @Autowired
    private RedisAclService aclService;
}
