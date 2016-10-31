package com.xtuer.controller;

import com.xtuer.bean.Result;
import com.xtuer.constant.UriView;
import com.xtuer.service.RedisAclService;
import com.xtuer.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 使用 Redis 实现的访问控制
 */
@Controller
public class RedisAclController {
    private static Logger logger = LoggerFactory.getLogger(RedisAclController.class);

    @Autowired
    private RedisAclService aclService;

    @GetMapping(UriView.URI_ACL)
    public String aclPage(ModelMap map) {
        map.put("currentCount", aclService.currentCount()); // 当前人数
        map.put("maxCount", aclService.maxCount()); // 最大允许访问人数
        map.put("maxDuration", aclService.maxDuration());

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
        long maxCount = aclService.maxCount(); // 访问人数限制

        // [1] 在 IP 列表中则继续
        if (aclService.inIpList(ip)) {
            logger.debug("Allowed, already in: " + ip);
            return Result.ok();
        }

        // [2] 不在 IP 列表，但是当前使用人数小于 maxCount，则加入，继续
        if (aclService.currentCount() < aclService.maxCount()) {
            logger.debug("Allowed, new added: " + ip);
            aclService.addToIpList(ip);
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
        return Result.ok(aclService.currentCount());
    }

    /**
     * 重置访问人数，即清空访问列表
     */
    @PutMapping(UriView.URI_ACL_RESET)
    @ResponseBody
    public Result reset() {
        aclService.reset();
        return Result.ok();
    }

    /**
     * 修改人数限制
     * @param maxCount 访问限制的人数
     */
    @PutMapping(UriView.URI_ACL_MAX_COUNT)
    @ResponseBody
    public Result setUpMaxCount(@PathVariable int maxCount, @RequestBody Map<String, String> map) {
        String password = map.get("password");
        return aclService.setUpMaxCount(password, maxCount);
    }

    /**
     * 修改每人最大停留时间
     * @param maxDuration 访问限制的人数
     */
    @PutMapping(UriView.URI_ACL_MAX_DURATION)
    @ResponseBody
    public Result setUpMaxDuration(@PathVariable int maxDuration, @RequestBody Map<String, String> map) {
        String password = map.get("password");
        return aclService.setUpMaxDuration(password, maxDuration);
    }
}
