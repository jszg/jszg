package com.xtuer.controller;

import com.xtuer.bean.Demo;
import com.xtuer.bean.EnrollmentForm;
import com.xtuer.bean.Result;
import com.xtuer.constant.UriView;
import com.xtuer.mapper.CertTypeMapper;
import com.xtuer.mapper.DemoMapper;
import com.xtuer.util.CommonUtils;
import com.xtuer.util.RedisUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;

@Controller
public class DemoController {
    // 1. 创建 logger 对象
    private static Logger logger = LoggerFactory.getLogger(DemoController.class.getName());

    @Autowired
    private DemoMapper demoMapper;

    @Autowired
    private CertTypeMapper certTypeMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Resource(name = "propertiesConfig")
    private PropertiesConfiguration propertiesConfig;

    @GetMapping("/demos/{id}")
    @ResponseBody
    public Demo findDemoById(@PathVariable int id) {
        // [1] 先从 Redis 查找
        // [2] 找到则返回
        // [3] 找不到则从数据库查询，查询结果放入 Redis
        Demo d = null;
        String redisKey = "demo_" + id;
        d = redisUtils.get(Demo.class, redisKey, () -> demoMapper.findDemoById(id));

        return d;
    }

    @GetMapping("/logback")
    @ResponseBody
    public String logback() {
        // 2. 和 log4j 一样使用
        logger.debug("No params");

        // 3. 可以使用 {} 的方式传入参数
        logger.debug("With params: time: {}, name: {}", System.nanoTime(), "Bingo");

        System.out.println(propertiesConfig.getString("uploadPersonImageDir" + "........"));
        System.out.println(propertiesConfig.getString("url"));

        return "Test logback";
    }

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Welcome";
    }

    @GetMapping(UriView.URI_HELLO)
    public String hello(ModelMap model) {
       model.put("name", "Biao");

        return UriView.VIEW_HELLO;
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

    @PostMapping("/validate")
    @ResponseBody
    public Result<?> validate(@RequestBody @Valid EnrollmentForm form, BindingResult result) {
        StringBuffer sb = new StringBuffer();

        for (FieldError error : result.getFieldErrors()) {
            sb.append(error.getField() + " : " + error.getDefaultMessage() + "\n");
        }

        if (result.hasErrors()) {
            return new Result(false, sb.toString());
        }

        // 验证日期
        try {
            Date graduationTime = DateUtils.parseDate(form.getGraduationTime(), "yyyy-MM-dd");
            Date beginWorkYear = DateUtils.parseDate(form.getBeginWorkYear(), "yyyy-MM-dd");
            Date workDate = DateUtils.parseDate(form.getWorkDate(), "yyyy-MM-dd");
        } catch (ParseException e) {
            return new Result(false, "时间格式错误，正确格式为 yyyy-MM-dd");
        }

        // 验证密码强度
        if (!CommonUtils.passwordHasEnoughStrength(form.getPassword())) {
            return new Result(false, "密码不少于 8 位，必须包含数字、字母和特殊字符，特殊字符需从 “#、%、*、-、_、!、@、$、&” 中选择");
        }

        return Result.ok(form);
    }
}
