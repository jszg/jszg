package com.xtuer.controller;

import com.xtuer.bean.EnrollmentForm;
import com.xtuer.bean.Result;
import com.xtuer.constant.SignUpConstants;
import com.xtuer.constant.UriView;
import com.xtuer.dto.Enrollment;
import com.xtuer.dto.HistoryValid;
import com.xtuer.dto.Registration;
import com.xtuer.mapper.CommonMapper;
import com.xtuer.service.EnrollmentService;
import com.xtuer.service.RedisAclService;
import com.xtuer.util.BrowserUtils;
import com.xtuer.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 注册的 Controller
 */
@Controller
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    RedisAclService redisAclService;

    @PostMapping(UriView.URI_ENROLL_SUBMIT)
    @ResponseBody
    public Result<?> submitEnroll(@RequestBody @Valid EnrollmentForm form, BindingResult result, HttpServletRequest request) {
        // [1] 数据验证
        Result<?> r = enrollmentService.validateParams(form, result);
        if (!r.isSuccess()) {
            return r;
        }

        List<Enrollment> status0 = commonMapper.findEnrollmentStatus0(form.getIdNo(), form.getCertNo());
        if (!status0.isEmpty()) {
            return new Result(false, "定期注册证件号码与证书号码重复提交");
        } else if (form.getInRegistration()) {
            Registration reg = commonMapper.findRegistrationById(form.getRegisterId());
            if (reg == null) {
                return new Result(false, "验证失败 Registration为空");
            }
            status0 = commonMapper.findEnrollmentStatus0(reg.getIdNo(), reg.getCertNo());
            if (!status0.isEmpty()) {
                return new Result(false, "定期注册证件号码与证书号码重复提交");
            }
        } else if (form.getInHistory()) {
            HistoryValid historyValid = commonMapper.findHistoryValidById(form.getRegisterId());
            if (historyValid == null) {
                return new Result(false, "验证失败 HistoryValid为空");
            }

            status0 = commonMapper.findEnrollmentStatus0(historyValid.getIdNo(), historyValid.getCertNo());
            if (!status0.isEmpty()) {
                return new Result(false, "定期注册证件号码与证书号码重复提交");
            }
        }

        // [2] 设置固定信息
        form.setConfirmStatus(SignUpConstants.STATUS_UN_CONF);
        form.setStatus(SignUpConstants.STATUS_UN_CERT);
        form.setReCheckStatus(SignUpConstants.STATUS_UN_CERT);
        form.setJudgmentStatus(SignUpConstants.STATUS_UN_CERT);
        form.setDataFrom(SignUpConstants.DATA_FROM_USER_ADD);
        form.setApplyTime(new Date());
        form.setIp(CommonUtils.getClientIp(request));
        form.setDeleteStatus(0);
        form.setBeginWorkYearInt(Integer.parseInt(form.getBeginWorkYear().substring(0, 4)));
        form.setPassword(CommonUtils.md5(form.getPassword())); // 使用 MD5 编码密码
//        form.setEnrollBatch(25); // TODO

        // [3] 使用注册机构查询市的信息
        form.setCityId(enrollmentService.getEnrollCityId(form.getOrgId()));

        // [4] 在认定历史表中
        if (form.getInHistory()) {
            enrollmentService.saveWhenInHistory(form);
        }

        // [5] 在认定表中
        if (form.getInRegistration()) {
            enrollmentService.saveWhenInRegistration(form);
        }

        // [6] 既不在认定历史表中，也不在认定表中
        if (!form.getInHistory() && !form.getInRegistration()) {
            enrollmentService.saveWhenNotInHistoryAndInRegistration(form);
        }

        // [7] 保存图片
        enrollmentService.saveEnrollPhoto(form);

        // [8] 写入日志
        enrollmentService.saveUserLog(form, request);

        // [9] remove from ip list
        String ip = CommonUtils.getClientIp(request);
        // removeFromIpList
        redisAclService.removeFromIpList(ip);
        return Result.ok(form);
    }
}
