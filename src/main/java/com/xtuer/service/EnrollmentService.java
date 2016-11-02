package com.xtuer.service;

import com.alibaba.fastjson.JSON;
import com.xtuer.bean.EnrollmentForm;
import com.xtuer.bean.RegistrationForm;
import com.xtuer.bean.Result;
import com.xtuer.bean.UserPortalLog;
import com.xtuer.constant.SignUpConstants;
import com.xtuer.dto.CertBatch;
import com.xtuer.dto.CertType;
import com.xtuer.dto.CityInfo;
import com.xtuer.dto.ProvinceBatch;
import com.xtuer.mapper.*;
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
import javax.servlet.Registration;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

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
    private OrganizationMapper organizationMapper;

    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private DictMapper dictMapper;

    public void saveWhenInHistory(EnrollmentForm form) {
        System.out.println("saveWhenInHistory");

        // 使用表中现有的认定历史表中数据
        EnrollmentForm temp = enrollmentMapper.findHistoryValidByRegisterId(form.getRegisterId());

        form.setRegisterId(form.getRegisterId());
        form.setIdNo(temp.getIdNo());
        form.setIdTypeId(temp.getIdTypeId());
        form.setName(temp.getName());
        form.setCertTypeId(temp.getCertTypeId());
        System.out.println(temp.getCertTypeId());
        System.out.println(temp.getSubjectId());
        form.setRegisterSubjectId(temp.getSubjectId());
        form.setCertNo(temp.getCertNo());
        form.setNationId(temp.getNationId());
        form.setGenderId(temp.getGenderId());
        form.setBirthday(temp.getBirthday());
        form.setCertAssignDate(temp.getCertAssignDate());
        form.setRecognizeOrgName(temp.getRecognizeOrgName());
        form.setSubjectId(temp.getSubjectId());
        form.setCityId(this.getEnrollCityId(form.getOrgId()));
        form.setStatusMemo("new-cert");
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
        form.setIdTypeId(temp.getIdTypeId());
        form.setCertTypeId(temp.getCertTypeId());
        form.setRegisterSubjectId(temp.getSubjectId());
        form.setIdNo(temp.getIdNo());
        form.setName(temp.getName());
        form.setNationId(temp.getNationId());
        form.setGenderId(temp.getGenderId());
        form.setBirthday(temp.getBirthday());
        form.setCertAssignDate(temp.getCertAssignDate());
        form.setRecognizeOrgName(temp.getRecognizeOrgName());
        form.setSubjectId(temp.getSubjectId());
        form.setCityId(this.getEnrollCityId(form.getOrgId()));
        form.setStatusMemo("new-cert");
        enrollmentMapper.insertEnrollment(form);
        System.out.println(JSON.toJSONString(form));
    }

    public void saveWhenNotInHistoryAndInRegistration(EnrollmentForm form){
        //首先给registration设置值
        RegistrationForm reg = new RegistrationForm();
        reg.setIdNo(form.getIdNo());
        reg.setCertNo(form.getCertNo());
        reg.setCertAssign(form.getCertAssignDate());
        try {
            reg.setCertAssignDate(DateUtils.parseDate(form.getCertAssignDate(),DATE_FORMAT));
            reg.setBirthdayDate(DateUtils.parseDate(form.getBirthday(),DATE_FORMAT));
            reg.setGraduaTimeDate(DateUtils.parseDate(form.getGraduationTime(),DATE_FORMAT));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reg.setCertBatchId(form.getCertBatchId());
        reg.setOrgId(form.getRecognizeOrgId());
        reg.setSubjectId(form.getRegisterSubjectId());
        reg.setName(form.getName());
        reg.setSex(form.getGenderId());
        reg.setNation(form.getNationId());
        reg.setAddress(form.getAddress());
        reg.setBirthPlace(form.getBirthPlace());
        reg.setCellPhone(form.getCellphone());
        reg.setDegreeId(form.getDegreeId());
        reg.setEduLevelId(form.getEduLevelId());
        reg.setEmail(form.getEmail());
        reg.setPassword(form.getPassword());
        reg.setGraduateSchool(form.getGraduationCollegeId());
        reg.setGraduateShoolName(form.getGraduationCollegeName());
        reg.setGraduaTime(form.getGraduationTime());
        reg.setLearnType(form.getLearnTypeId());
        reg.setMajorId(form.getMajorId());
        reg.setNormalMajor(form.getNormalMajorId());
        reg.setPhone(form.getPhone());
        reg.setPolitical(form.getPoliticalId());
        reg.setPthevelId(form.getPthLevelId());
        reg.setResidence(form.getResidence());
        reg.setTechniqueJobId(form.getTechnicalJobId());
        reg.setWorkUnits(form.getWorkUnit());
        reg.setZipCode(form.getZipCode());
        reg.setExam(SignUpConstants.EXAM_TYPE_NO_EXAM );
        reg.setStatus(30);
        reg.setDataFrom(2);
        reg.setDeleteStatus(0);
        reg.setOrgId(form.getRecognizeOrgId());
        reg.setOrgName(form.getRecognizeOrgName());
        reg.setProvinceId(organizationMapper.findProvinceByOrgId(form.getRecognizeOrgId()).getProvinceId());
        reg.setOccupation(dictMapper.findByTypeAndCode(4,20).getId());
        reg.setIp(form.getIp());
        if(this.getRegCityId(form.getRecognizeOrgId()) != null){
            reg.setCityId(this.getRegCityId(form.getRecognizeOrgId()));
        }
        int year = CommonUtils.getCertYearFromRegistration(form.getCertNo(),form.getCertAssignDate());
        CertBatch certBatch = commonMapper.findByYear(year);
        if(certBatch != null){
            reg.setCertBatchId(certBatch.getId());
        }

        reg.setEnrollProBatchId(commonMapper.findByProvinceId(form.getProvinceId()).getId());
        reg.setCertType(form.getCertTypeId());
        reg.setIdType(form.getIdTypeId());
        reg.setStatusMemo("new-cert");
        //保存registration
        registrationMapper.insertRegistration(reg);
        //给enrollment设置值，并会写regId
        form.setRegisterId(reg.getRegId());
        form.setCityId(this.getEnrollCityId(form.getOrgId()));
        form.setStatusMemo("new-cert");
        enrollmentMapper.insertEnrollment(form);
        System.out.println("saveWhenNotInHistoryAndInRegistration");
    }

    /**
     * 保存 Enroll 的图片
     * @param form
     */
    public void saveEnrollPhoto(EnrollmentForm form) {
        String tempDir = config.getString("uploadTemp"); // 图片的临时目录
        String tempName = form.getTmpPhoto();
        String tempPhotoPath = tempDir + File.separator + tempName; // 临时图片路径
        String photoPath = generateEnrollPhotoPath(form.getEnrollId());

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
     * @param enrollId
     * @return 图片路径
     */
    public String generateEnrollPhotoPath(long enrollId) {
        String photoDir = config.getString("uploadEnrollPhotoDir"); // 图片的最终目录
        String photoName = String.format("%010d.jpg", enrollId);
        String photoPath = photoDir + File.separator + photoName.substring(0,2) + File.separator +
                photoName.substring(2,6) + File.separator + photoName.substring(6);

        return photoPath;
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
