package com.xtuer.controller;

import com.xtuer.bean.RegistrationForm;
import com.xtuer.bean.Result;
import com.xtuer.constant.SignUpConstants;
import com.xtuer.constant.UriView;
import com.xtuer.mapper.CommonMapper;
import com.xtuer.mapper.RegistrationMapper;
import com.xtuer.service.RedisAclService;
import com.xtuer.service.RegistrationService;
import com.xtuer.util.CommonUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    private RegistrationMapper registrationMapper;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    RedisAclService redisAclService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(UriView.URI_REQUEST_SUBMIT)
    @ResponseBody
    public Result<?> submitRequest(@RequestBody @Valid RegistrationForm form, BindingResult result, HttpServletRequest request,@RequestParam String token) {
        // [1] 数据验证
        Result<?> r = registrationService.validateParams(form, result);
        if (!r.isSuccess()) {
            return r;
        }
        //验证certbatch idNo name 联合唯一
        List<RegistrationForm> regList = registrationMapper.findByCbIdAndIdNoAndName(form.getName(),form.getIdNo(),form.getCertBatchId());
        if(!regList.isEmpty()){
            return new Result(false, "同一批次下已经存在相同的名称和证件号的记录");
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
        form.setScoreCertNo("");
        form.setPrintStatus(0);
        // [3] 保存数据
        try{
            registrationService.save(form);
        }catch (Exception ex) {
            return Result.error("认定报名失败!","");
        }
        // [4] 保存简历信息
        try{
            registrationService.saveResum(form);
        }catch (Exception ex) {
            return Result.error("认定报名保存简历信息失败!","");
        }

        // [5] 保存图片
        try{
            registrationService.saveRequestPhoto(form);
        }catch (Exception ex) {
            return Result.error("认定报名保存图片失败!","");
        }
        // [6] 写入日志
        try{
            registrationService.saveUserLog(form, request);
        }catch (Exception ex) {
            return Result.error("认定报名写入日志失败!","");
        }

        // [8] remove from ip list
        String ip = CommonUtils.getClientIp(request);
        // removeFromIpList
        redisAclService.removeFromIpList(ip);
        redisTemplate.delete(token);
        return Result.ok(form);
    }
}
