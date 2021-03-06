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
        return aclService.canAccess(ip) ? Result.ok() : Result.error();
    }

    /**
     * 判断用户是否可以访问
     * 访问使用 JSONP
     */
    @GetMapping(UriView.URI_ACL_CAN_ACCESS_JSONP)
    @ResponseBody
    public String canAccessJsonp(@RequestParam String jsonpCallback, HttpServletRequest request) {
        String ip = CommonUtils.getClientIp(request);
        return String.format("%s({\"success\": %b})", jsonpCallback, aclService.canAccess(ip));
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
