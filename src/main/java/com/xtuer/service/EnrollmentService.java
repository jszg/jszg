package com.xtuer.service;

import com.alibaba.fastjson.JSON;
import com.xtuer.bean.EnrollmentForm;
import com.xtuer.bean.Result;
import com.xtuer.bean.UserPortalLog;
import com.xtuer.constant.SignUpConstants;
import com.xtuer.dto.CityInfo;
import com.xtuer.dto.ProvinceBatch;
import com.xtuer.mapper.CommonMapper;
import com.xtuer.mapper.EnrollmentMapper;
import com.xtuer.mapper.RegistrationMapper;
import com.xtuer.util.BrowserUtils;
import com.xtuer.util.CommonUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

@Service
public class EnrollmentService {
    private static Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Resource(name = "config")
    private PropertiesConfiguration config;

    @Autowired
    private RegistrationMapper registrationMapper;

    public void saveWhenInHistory(EnrollmentForm form) {
        System.out.println("saveWhenInHistory");

        // 使用表中现有的认定历史表中数据
        EnrollmentForm temp = enrollmentMapper.findHistoryValidByRegisterId(form.getRegisterId());

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
        //从认定申请表来的数据在定期注册时,要向认定表中回写省级注册计划信息
        ProvinceBatch pb = commonMapper.findByProvinceId(form.getProvinceId());
        registrationMapper.updateEnrollProBatch(pb.getId(),form.getRegisterId());

        // 使用表中现有认定正式表的数据
        EnrollmentForm temp = enrollmentMapper.findRegistrationByRegisterId(form.getRegisterId());
        form.setRegisterId(temp.getRegisterId());
        form.setCertNo(temp.getCertNo());
        form.setCertTypeId(temp.getCertTypeId());
        form.setSubjectId(temp.getSubjectId());
        form.setIdNo(temp.getIdNo());
        form.setName(temp.getName());
        form.setNationId(temp.getNationId());
        form.setGenderId(temp.getGenderId());
        form.setBirthday(temp.getBirthday());
        form.setCertAssignDate(temp.getCertAssignDate());
        form.setRecognizeOrgName(temp.getRecognizeOrgName());

        enrollmentMapper.insertEnrollment(form);
        System.out.println(JSON.toJSONString(form));
    }

    public void saveWhenNotInHistoryAndInRegistration(EnrollmentForm form) {

        System.out.println("saveWhenNotInHistoryAndInRegistration");
    }

    public void saveEnrollPhoto(EnrollmentForm form) {
        // 初始文件名为 enrollId 补齐 0 到 10 字符加图片后缀
        // 保存路径: 0000001234.jpg to 00/0001/2345.jpg
        String tempDir = config.getString("uploadTemp"); // 图片的临时目录
        String photoDir = config.getString("uploadEnrollPhotoDir"); // 图片的最终目录

        String tempName = form.getTmpPhoto();
        String photoName = String.format("%010d", form.getEnrollId()) + "." + FilenameUtils.getExtension(tempName);
        String tempPhotoPath = tempDir + File.separator + tempName; // 临时图片路径
        String photoPath = photoDir + File.separator + photoName.substring(0,2) + File.separator +
                photoName.substring(2,6) + File.separator + photoName.substring(6);

        try {
            FileUtils.moveFile(new File(tempPhotoPath), new File(photoPath));
        } catch (IOException e) {
            logger.warn("移动图片失败: {}", e.getMessage());
        }
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
