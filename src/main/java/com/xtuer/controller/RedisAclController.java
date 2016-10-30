package com.xtuer.controller;

import com.xtuer.bean.Result;
import com.xtuer.constant.RedisKey;
import com.xtuer.constant.UriView;
import com.xtuer.util.CommonUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 使用 Redis 实现的访问控制
 */
@Controller
public class RedisAclController {
    @GetMapping(UriView.URI_ACL)
    public String aclPage(ModelMap map) {
        map.put("currentCount", currentCount()); // 当前人数
        map.put("maxCount", maxCount()); // 最大允许访问人数

        return "acl.fm";
    }

    /**
     * 判断用户是否可以访问
     *
     * @return 可以访问返回 Result.ok()，否则返回 Result.error()
     */
    @GetMapping(UriView.URI_ACL_CAN_ACCESS)
    @ResponseBody
    public Result canAccess(HttpServletRequest request) {
        String ip = CommonUtils.getClientIp(request);
        int maxCount = config.getInteger("aclCount", 2000); // 访问人数限制

        // [1] 在 IP 列表中则继续
        if (redisTemplate.opsForZSet().rank(RedisKey.ACL_KEY, ip) != null) {
            logger.debug("Allowed, already in: " + ip);
            return Result.ok();
        }

        // [2] 不在 IP 列表，但是小于 maxCount，则加入，继续
        if (redisTemplate.opsForZSet().rank(RedisKey.ACL_KEY, ip) == null
                && redisTemplate.opsForZSet().size(RedisKey.ACL_KEY) < maxCount) {
            logger.debug("Allowed, new added: " + ip);
            redisTemplate.opsForZSet().add(RedisKey.ACL_KEY, ip, System.currentTimeMillis());
            return Result.ok();
        }

        // [3] 既不在 IP 列表，同时 IP 列表超过了 maxCount，则不许访问
        logger.debug("Not allowed: " + ip);
        return Result.error();
    }

    /**
     * 查询当前访问人数
     */
    @GetMapping(UriView.URI_ACL_COUNT)
    @ResponseBody
    public Result<Long> count() {
        return Result.ok(currentCount());
    }

    /**
     * 重置访问人数，即清空访问列表
     */
    @PutMapping(UriView.URI_ACL_RESET)
    @ResponseBody
    public Result reset() {
        redisTemplate.delete(RedisKey.ACL_KEY);
        return Result.ok();
    }

    /**
     * 修改人数限制
     * @param count 访问限制的人数
     */
    @PutMapping(UriView.URI_ACL_COUNT_SET_UP)
    @ResponseBody
    public Result setUpCount(@PathVariable int count, @RequestBody Map<String, String> map) {
        String password = map.get("password");

        if (password == null || !password.equals(config.getString("aclPassword"))) {
            return Result.error();
        }

        count = count > 0 ? count : 2000;
        config.setProperty("aclCount", count);
        logger.debug("访问人数限制到 {}", maxCount());

        return Result.ok();
    }

    private long currentCount() {
        return redisTemplate.opsForZSet().size(RedisKey.ACL_KEY);
    }

    private long maxCount() {
        return config.getInteger("aclCount", 2000);
    }

    private static Logger logger = LoggerFactory.getLogger(RedisAclController.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource(name = "config")
    private PropertiesConfiguration config;
}
