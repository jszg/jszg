package com.xtuer.controller;

import com.xtuer.bean.EnrollmentForm;
import com.xtuer.bean.Result;
import com.xtuer.constant.SignUpConstants;
import com.xtuer.constant.UriView;
import com.xtuer.service.EnrollmentService;
import com.xtuer.util.BrowserUtils;
import com.xtuer.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * 注册的 Controller
 */
@Controller
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping(UriView.URI_ENROLL_SUBMIT)
    @ResponseBody
    public Result<?> submitEnroll(@RequestBody @Valid EnrollmentForm form, BindingResult result, HttpServletRequest request) {
        // [1] 数据验证
        Result<?> r = enrollmentService.validateParams(form, result);
        if (!r.isSuccess()) {
            return r;
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
        form.setIdTypeId(36);
        form.setEnrollBatch(25);

        form.setBeginWorkYearInt(Integer.parseInt(form.getBeginWorkYear().substring(0, 4)));

        // [3] 查询校验市的信息
        enrollmentService.verifyCityInfo(form);

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

        // [8] 写入日志
        enrollmentService.saveUserLog(form, request);

        System.out.println(BrowserUtils.getBrowserName(request));

        return Result.ok(form);
    }
}
