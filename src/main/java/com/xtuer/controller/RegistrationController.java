package com.xtuer.controller;

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
    public Result<?> submitEnroll(@RequestBody @Valid RegistrationForm form, BindingResult result, HttpServletRequest request) {
        // [1] 数据验证
        Result<?> r = registrationService.validateParams(form, result);
        if (!r.isSuccess()) {
            return r;
        }

        // [2] 设置固定信息

        // [3] 保存简历信息

        // [4] 保存数据
        registrationService.save(form,request);

        // [5] 保存图片
        registrationService.saveRequestPhoto(form);

        // [6] 写入日志
        registrationService.saveUserLog(form, request);

        // [6] 写入日志
        registrationService.saveResum(form);

        // [8] remove from ip list
        String ip = CommonUtils.getClientIp(request);
        // removeFromIpList
        redisAclService.removeFromIpList(ip);
        return Result.ok(form);
    }

    /**
     * 访问注册用户的图片
     * @param regId
     * @param response
     */
    @GetMapping(UriView.URI_REQUEST_REG_PHOTO)
    public void enrollRegPhoto(@PathVariable long regId, HttpServletResponse response) {
        String photoDir = config.getString("uploadRegPhotoDir"); // 图片的最终目录
        String photoPath = registrationService.generateEnrollPhotoPath(regId, photoDir);
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(photoPath);
            out = response.getOutputStream();
            IOUtils.copy(in, out);
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
}
