package com.xtuer.service;

import com.alibaba.fastjson.JSON;
import com.xtuer.bean.EnrollmentForm;
import com.xtuer.bean.RegistrationForm;
import com.xtuer.bean.Result;
import com.xtuer.bean.UserPortalLog;
import com.xtuer.constant.SignUpConstants;
import com.xtuer.dto.CertBatch;
import com.xtuer.dto.CityInfo;
import com.xtuer.dto.ProvinceBatch;
import com.xtuer.mapper.*;
import com.xtuer.util.BrowserUtils;
import com.xtuer.util.CommonUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
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
public class RegistrationService {
    private static Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Resource(name = "config")
    private PropertiesConfiguration config;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private DictMapper dictMapper;

    public void save(RegistrationForm form,HttpServletRequest request){
        //首先给registration设置值
        RegistrationForm reg = new RegistrationForm();

        reg.setProvinceId(form.getProvinceId());
        reg.setCertType(form.getCertType());
        reg.setCityId(form.getCityId());
        reg.setOrgId(form.getOrgId());
        reg.setOrgName(form.getOrgName());
        reg.setLocaleSet(form.getLocaleSet());
        reg.setSubjectId(form.getSubjectId());
        reg.setCertBatchId(form.getCertBatchId());
        reg.setIdNo(form.getIdNo());
        reg.setName(form.getName());
        reg.setIdType(form.getIdType());
        reg.setEmail(form.getEmail());
        reg.setPassword(form.getPassword());
        reg.setSex(form.getSex());
        reg.setBirthdayDate(form.getBirthdayDate());
        reg.setEduLevelId(form.getEduLevelId());
        reg.setDegreeId(form.getDegreeId());
        reg.setNation(form.getNation());
        reg.setMajorId(form.getMajorId());
        reg.setOccupation(form.getOccupation());
        reg.setTechniqueJobId(form.getTechniqueJobId());
        reg.setPolitical(form.getPolitical());
        reg.setPthevelId(form.getPthevelId());
        reg.setGraduateSchool(form.getGraduateSchool());
        reg.setLearnType(form.getLearnType());
        reg.setGraduaTimeDate(form.getGraduaTimeDate());
        reg.setGraduateId(form.getGraduateId());
        reg.setResidence(form.getResidence());
        reg.setBirthPlace(form.getBirthPlace());
        reg.setAddress(form.getAddress());
        reg.setZipCode(form.getZipCode());
        reg.setPhone(form.getPhone());
        reg.setCellPhone(form.getCellPhone());
        reg.setWorkUnits(form.getWorkUnits());
        reg.setApplyTime(new Date());
        reg.setDeleteStatus(0);// 正常
        reg.setStatus(6);// 网报待确认
        reg.setLastModify(new Date());
        reg.setLastModifier(reg.getIdNo());
        reg.setTriggerTime(new Date());
        reg.setIp(CommonUtils.getClientIp(request));
        reg.setStatusMemo("new-cert");
        reg.setDataFrom(0);
        reg.setNormalMajor(form.getNormalMajor());
        reg.setExam(0);
        reg.setPthCertNo(form.getPthCertNo());
        reg.setPthOrg(form.getPthOrg());














        registrationMapper.insertRegistration(reg);
    }

    public void saveResum(RegistrationForm form){
        //首先给registration设置值
        RegistrationForm reg = new RegistrationForm();

        registrationMapper.insertRegistration(reg);
    }

    /**
     * 保存 Enroll 的图片
     * @param form
     */
    public void saveRequestPhoto(RegistrationForm form) {
        String tempDir = config.getString("uploadTemp"); // 图片的临时目录
        String tempName = form.getTmpPhoto();
        String tempPhotoPath = tempDir + File.separator + tempName; // 临时图片路径
        String photoDir = config.getString("uploadEnrollPhotoDir"); // 图片的最终目录
        String photoPath = generateEnrollPhotoPath(form.getRegId(), photoDir);

        try {
            FileUtils.moveFile(new File(tempPhotoPath), new File(photoPath));
        } catch (IOException e) {
            logger.warn("移动图片失败: {}", e.getMessage());
        }
    }

    /**
     * 根据 enrollId 创建注册使用的图片路径
     * 初始文件名为 enrollId 补齐 0 到 10 字符加图片后缀
     * 保存路径: 0000001234.jpg to 00/0001/2345.jpg
     *
     * @param regId
     * @return 图片路径
     */
    public String generateEnrollPhotoPath(long regId, String dir) {
        String photoName = String.format("%010d.jpg", regId);
        String photoPath = dir + File.separator + photoName.substring(0,2) + File.separator +
                photoName.substring(2,6) + File.separator + photoName.substring(6);

        return photoPath;
    }

    public void saveUserLog(RegistrationForm form, HttpServletRequest request) {
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
     * 获取注册机构所在市的 id
     *
     * @param orgId 机构的 id
     * @return 市的 id
     */
    public int getEnrollCityId(int orgId) {
        CityInfo cityInfo = commonMapper.findCityInfoByOrgId(orgId);

        // 如果是省或者市，返回自己
        if (cityInfo.getOrgType() == SignUpConstants.T_PROVINCE || cityInfo.getOrgType() == SignUpConstants.T_CITY) {
            return orgId;
        }

        // 如果是县
        if (cityInfo.getOrgType() == SignUpConstants.T_COUNTY) {
            // 如果上级机构是省(省管县)
            if (cityInfo.getParentId() != 0 && cityInfo.getParentOrgType() == SignUpConstants.T_PROVINCE) {
                // 如果是直辖市返回父级机构，否则返回自己
                if (cityInfo.isProvinceCity()) {
                    return cityInfo.getParentId();
                } else {
                    return orgId;
                }
            } else {
                // 如果不是省级机构(市管县)，返回自己
                return cityInfo.getParentId();
            }
        }

        return orgId;
    }

    /**
     * 获取认定机构所在市的 id
     *
     * @param orgId 机构的 id
     * @return 市的 id
     */
    public Integer getRegCityId(int orgId) {
        CityInfo cityInfo = commonMapper.findCityInfoByOrgId(orgId);

        // 如果是省，返回空
        if (cityInfo.getOrgType() == SignUpConstants.T_PROVINCE){
            return null;
        }

        // 如果是市，返回自己
        if(cityInfo.getOrgType() == SignUpConstants.T_CITY){
            return orgId;
        }

        // 如果是县
        if (cityInfo.getOrgType() == SignUpConstants.T_COUNTY) {
            // 如果上级机构是省(省管县)
            if (cityInfo.getParentId() != 0 && cityInfo.getParentOrgType() == SignUpConstants.T_PROVINCE) {
                // 如果是直辖市返回父级机构，否则返回自己
                if (cityInfo.isProvinceCity()) {
                    return cityInfo.getParentId();
                } else {
                    return orgId;
                }
            } else {
                // 如果不是省级机构(市管县)，返回自己
                return cityInfo.getParentId();
            }
        }
        return orgId;
    }

    public Result<?> validateParams(RegistrationForm form, BindingResult result) {
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
            Date graduationTime = DateUtils.parseDate(form.getGraduaTime(), DATE_FORMAT);
            Date birthday = DateUtils.parseDate(form.getBirthday(), DATE_FORMAT);
            form.setGraduaTimeDate(graduationTime);
            form.setBirthdayDate(birthday);
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
