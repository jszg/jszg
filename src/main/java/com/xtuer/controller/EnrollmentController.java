package com.xtuer.controller;

import com.xtuer.bean.EnrollmentForm;
import com.xtuer.bean.Result;
import com.xtuer.constant.SignUpConstants;
import com.xtuer.constant.UriView;
import com.xtuer.dto.Enrollment;
import com.xtuer.dto.HistoryValid;
import com.xtuer.dto.ProvinceBatch;
import com.xtuer.dto.Registration;
import com.xtuer.mapper.CommonMapper;
import com.xtuer.service.EnrollmentService;
import com.xtuer.service.RedisAclService;
import com.xtuer.util.CommonUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
public class EnrollmentController {
    private static Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    @Resource(name = "config")
    private PropertiesConfiguration config;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    RedisAclService redisAclService;

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(UriView.URI_ENROLL_SUBMIT)
    @ResponseBody
    public Result<?> submitEnroll(@RequestBody @Valid EnrollmentForm form, BindingResult result, HttpServletRequest request,@RequestParam String token) {
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

    /**
     * 访问注册用户的图片
     * @param enrollId
     * @param response
     */
    @GetMapping(UriView.URI_ENROLL_REG_PHOTO)
    public void enrollRegPhoto(@PathVariable long enrollId, HttpServletResponse response) {
        String photoDir = config.getString("uploadRegPhotoDir"); // 图片的最终目录
        String photoPath = enrollmentService.generateEnrollPhotoPath(enrollId, photoDir);
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
