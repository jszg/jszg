package com.xtuer.controller;

import com.alibaba.fastjson.JSON;
import com.xtuer.bean.EnrollmentForm;
import com.xtuer.bean.RegistrationForm;
import com.xtuer.bean.Result;
import com.xtuer.constant.SignUpConstants;
import com.xtuer.constant.UriView;
import com.xtuer.dto.Enrollment;
import com.xtuer.dto.HistoryValid;
import com.xtuer.dto.Registration;
import com.xtuer.mapper.CommonMapper;
import com.xtuer.service.EnrollmentService;
import com.xtuer.service.RedisAclService;
import com.xtuer.service.RegistrationService;
import com.xtuer.util.CommonUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * 注册的 Controller
 */
@Controller
public class RegistrationController {
    private static Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Resource(name = "config")
    private PropertiesConfiguration config;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    RedisAclService redisAclService;

    @PostMapping(UriView.URI_REQUEST_SUBMIT)
    @ResponseBody
    public Result<?> submitRequest(@RequestBody @Valid RegistrationForm form, BindingResult result, HttpServletRequest request) {
        // [1] 数据验证
        Result<?> r = registrationService.validateParams(form, result);
        if (!r.isSuccess()) {
            return r;
        }
        // [2] 设置固定信息
        form.setApplyTime(new Date());
        form.setDeleteStatus(SignUpConstants.DELETE_STATUS_NORMAL);// 正常
        form.setStatus(SignUpConstants.STATUS_UN_CONF);// 网报待确认
        form.setLastModify(new Date());
        form.setLastModifier(form.getIdNo());
        form.setTriggerTime(new Date());
        form.setIp(CommonUtils.getClientIp(request));
        form.setStatusMemo("new-cert");
        form.setDataFrom(SignUpConstants.DATA_FROM_USER_ADD);
        form.setExam(SignUpConstants.EXAM_TYPE_NO_EXAM);
        form.setPassword(CommonUtils.md5(form.getPassword())); // 使用 MD5 编码密码
        // [3] 保存数据
        registrationService.save(form,request);
        System.out.println(JSON.toJSONString(form));

        // [4] 保存简历信息
        //registrationService.saveResum(form);

        // [5] 保存图片
        registrationService.saveRequestPhoto(form);

        // [6] 写入日志
        registrationService.saveUserLog(form, request);

        // [8] remove from ip list
        String ip = CommonUtils.getClientIp(request);
        // removeFromIpList
        redisAclService.removeFromIpList(ip);
        return Result.ok(form);
    }
}
