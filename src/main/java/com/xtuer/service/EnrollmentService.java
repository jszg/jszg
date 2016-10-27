package com.xtuer.service;

import com.alibaba.fastjson.JSON;
import com.xtuer.bean.EnrollmentForm;
import com.xtuer.bean.Result;
import com.xtuer.bean.UserPortalLog;
import com.xtuer.constant.SignUpConstants;
import com.xtuer.dto.CityInfo;
import com.xtuer.mapper.CommonMapper;
import com.xtuer.mapper.EnrollmentMapper;
import com.xtuer.util.BrowserUtils;
import com.xtuer.util.CommonUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;

@Service
public class EnrollmentService {
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    public void saveWhenInHistory(EnrollmentForm form) {
        System.out.println("saveWhenInHistory");
        // 使用表中现有的数据
        EnrollmentForm temp = enrollmentMapper.findByRegisterId(form.getRegisterId());

        form.setRegisterId(temp.getRegisterId());
        form.setIdNo(temp.getIdNo());
        form.setName(temp.getName());
        form.setCertTypeId(temp.getCertTypeId());
        form.setSubjectId(temp.getSubjectId());
        form.setCertNo(temp.getCertNo());
        form.setNationId(temp.getNationId());
        form.setGenderId(temp.getGenderId());
        form.setBirthday(temp.getBirthday());
        form.setCertAssignDate(temp.getCertAssignDate());
        form.setRecognizeOrgName(temp.getRecognizeOrgName());

        enrollmentMapper.insertEnrollment(form);

        System.out.println(JSON.toJSONString(form));
    }

    public void saveWhenInRegistration(EnrollmentForm form) {
        System.out.println("saveWhenInRegistration");
    }

    public void saveWhenNotInHistoryAndInRegistration(EnrollmentForm form) {
        System.out.println("saveWhenNotInHistoryAndInRegistration");
    }

    public void saveUserLog(EnrollmentForm form, HttpServletRequest request) {
        UserPortalLog userPortalLog = new UserPortalLog();
        userPortalLog.setUserId(form.getIdNo());
        userPortalLog.setLogin(new Date());
        userPortalLog.setIp(form.getIp());
        userPortalLog.setType(UserPortalLog.ENROLL_SUBMIT);
        userPortalLog.setBrowserContent(BrowserUtils.getBrowserContent(request));
        userPortalLog.setBrowserName(BrowserUtils.getBrowserName(request));

        commonMapper.insertUserPortalLog(userPortalLog);
    }

    /**
     * 查询校验市的信息
     *
     * @param form
     */
    public void verifyCityInfo(EnrollmentForm form) {
        CityInfo cityInfo = commonMapper.findCityInfoByOrgId(form.getOrgId());
        if (cityInfo.getOrgType() == SignUpConstants.T_PROVINCE) {
            form.setCityId(form.getOrgId());
        } else {
            if (cityInfo.getOrgType() == SignUpConstants.T_CITY) {
                form.setCityId(form.getOrgId());
            } else if (cityInfo.getOrgType() == SignUpConstants.T_COUNTY) {
                if (cityInfo.getParentId() != 0 && cityInfo.getParentOrgType() == SignUpConstants.T_PROVINCE) {
                    if (cityInfo.isProvinceCity()) {
                        form.setCityId(cityInfo.getParentId());
                    } else {
                        form.setCityId(form.getOrgId());
                    }
                } else {
                    form.setCityId(cityInfo.getParentId());
                }
            }
        }
    }

    public Result<?> validateParams(EnrollmentForm form, BindingResult result) {
        StringBuffer sb = new StringBuffer();

        for (FieldError error : result.getFieldErrors()) {
            sb.append(error.getField() + " : " + error.getDefaultMessage() + "\n");
        }

        if (result.hasErrors()) {
            return new Result(false, sb.toString());
        }

        // 验证日期
        try {
            // 例如 2012-22-12-12 也是能够被转换为日期，所以最后需要再格式化回去为合法的日期
            Date graduationTime = DateUtils.parseDate(form.getGraduationTime(), DATE_FORMAT);
            Date beginWorkYear = DateUtils.parseDate(form.getBeginWorkYear(), DATE_FORMAT);
            Date workDate = DateUtils.parseDate(form.getWorkDate(), DATE_FORMAT);
            Date birthday = DateUtils.parseDate(form.getBirthday(), DATE_FORMAT);
            Date certAssignDate = DateUtils.parseDate(form.getCertAssignDate(), DATE_FORMAT);

            form.setGraduationTimeDate(graduationTime);
            form.setBeginWorkYearDate(beginWorkYear);
            form.setWorkDateDate(workDate);
            form.setBirthdayDate(birthday);
            form.setCertAssignDateDate(certAssignDate);
        } catch (ParseException e) {
            return new Result(false, "时间格式错误，正确格式为 yyyy-MM-dd");
        }

        // 验证密码强度
        if (!CommonUtils.passwordHasEnoughStrength(form.getPassword())) {
            return new Result(false, "密码不少于 8 位，必须包含数字、字母和特殊字符，特殊字符需从 “#、%、*、-、_、!、@、$、&” 中选择");
        }

        return Result.ok();
    }
}
