package com.xtuer.controller;

import com.alibaba.fastjson.JSON;
import com.xtuer.bean.Demo;
import com.xtuer.bean.Result;
import com.xtuer.mapper.CertTypeMapper;
import com.xtuer.mapper.DemoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {
    // 1. 创建 logger 对象
    private static Logger logger = LoggerFactory.getLogger(DemoController.class.getName());

    @Autowired
    private DemoMapper demoMapper;

    @Autowired
    private CertTypeMapper certTypeMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/demos/{id}")
    @ResponseBody
    public Demo findDemoById(@PathVariable int id) {
        // [1] 先从 Redis 查找
        // [2] 找到则返回
        // [3] 找不到则从数据库查询，查询结果放入 Redis
        Demo d = null;
        String redisKey = "demo_" + id;
        logger.debug("====================");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        logger.debug("====================");
//        for (int i = 0; i < 10; ++i)
//        d = RedisUtils.get(Demo.class, redisTemplate, redisKey, () -> demoMapper.findDemoById(id));

//        for (int i = 0; i < 10; ++i)
//        d = demoMapper.findDemoById(id);

        System.out.println(JSON.toJSONString(certTypeMapper.findAll()));

        return d;
    }

    @GetMapping("/logback")
    @ResponseBody
    public String logback() {
        // 2. 和 log4j 一样使用
        logger.debug("No params");

        // 3. 可以使用 {} 的方式传入参数
        logger.debug("With params: time: {}, name: {}", System.nanoTime(), "Bingo");

        return "Test logback";
    }

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Welcome";
    }

    @GetMapping(UriViewConstants.URI_HELLO)
    public String hello(ModelMap model) {
       model.put("name", "Biao");

        return UriViewConstants.VIEW_HELLO;
    }

    @GetMapping("/ajax")
    @ResponseBody // 处理 AJAX 请求，返回响应的内容，而不是 View Name
    public String ajaxString() {
        return "{\"username\": \"Josh\", \"password\": \"Passw0rd\"}";
    }

    @GetMapping("/ajax-object")
    @ResponseBody
    public Result ajaxObject() {
        return new Result(true, "你好");
    }
}
