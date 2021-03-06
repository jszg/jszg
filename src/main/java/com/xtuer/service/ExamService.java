package com.xtuer.service;

import com.xtuer.bean.RegistrationForm;
import com.xtuer.bean.Result;
import com.xtuer.bean.UserPortalLog;
import com.xtuer.constant.SignUpConstants;
import com.xtuer.dto.Organization;
import com.xtuer.dto.Resume;
import com.xtuer.dto.Score;
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
public class ExamService {
    private static Logger logger = LoggerFactory.getLogger(ExamService.class);

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
        try {
            form.setBirthdayDate(DateUtils.parseDate(form.getBirthDay(),DATE_FORMAT));
            form.setGraduaTimeDate(DateUtils.parseDate(form.getGraduaTime(),DATE_FORMAT));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        form.setProvinceId(form.getProvinceId());
        form.setCertType(form.getCertType());
        form.setCityId(form.getCityId());
        form.setOrgId(form.getOrgId());
        form.setOrgName(form.getOrgName());
        form.setLocaleId(form.getLocaleId());
        form.setLocaleSet(form.getLocaleSet());
        form.setSubjectId(form.getSubjectId());
        form.setCertBatchId(form.getCertBatchId());
        form.setIdNo(form.getIdNo());
        form.setName(form.getName());
        form.setIdType(form.getIdType());
        form.setEmail(form.getEmail());
        form.setPassword(form.getPassword());
        form.setSex(form.getSex());
        form.setBirthdayDate(form.getBirthdayDate());
        form.setEduLevelId(form.getEduLevelId());
        form.setDegreeId(form.getDegreeId());
        form.setNation(form.getNation());
        form.setMajorId(form.getMajorId());
        form.setOccupation(form.getOccupation());
        form.setTechniqueJobId(form.getTechniqueJobId());
        form.setPolitical(form.getPolitical());
        form.setPthevelId(form.getPthevelId());
        form.setGraduateSchool(form.getGraduateSchool());
        form.setLearnType(form.getLearnType());
        form.setGraduaTimeDate(form.getGraduaTimeDate());
        form.setGraduateId(form.getGraduateId());
        form.setResidence(form.getResidence());
        form.setBirthPlace(form.getBirthPlace());
        form.setAddress(form.getAddress());
        form.setZipCode(form.getZipCode());
        form.setPhone(form.getPhone());
        form.setCellPhone(form.getCellPhone());
        form.setWorkUnits(form.getWorkUnits());
        form.setNormalMajor(form.getNormalMajor());
        form.setPthCertNo(form.getPthCertNo());
        form.setPthOrg(form.getPthOrg());
        Organization org = commonMapper.findCityInfoByOrgId(form.getOrgId());
        if(org != null){
            if (org.getOrgType() == SignUpConstants.T_PROVINCE){//如果认定机构选择的是省级机构,所在省为本机构,所在市为空,认定机构为本机构
                form.setProvinceId(org.getId());
                form.setCityId(null);
            }else if(org.getOrgType() == SignUpConstants.T_CITY){//如果是市级机构,所在省为省级机构,所在市为本机构,认定机构为本机构
                form.setProvinceId(org.getProvinceId());
                form.setCityId(org.getId());
            }else if(org.getOrgType() == SignUpConstants.T_COUNTY){//如果是县级机构
                form.setProvinceId(org.getProvinceId());
                //如果是省管县,所在省为省级机构,市级机构为本机构,认定机构为本机构
                if(org.getParent() != 0) {
                    Organization parent = commonMapper.findCityInfoByOrgId(org.getParent());
                    if (parent != null && parent.getOrgType() == SignUpConstants.T_PROVINCE) {//如果为省管县
                        if(parent.isProvinceCity() == true){
                            form.setCityId(org.getParent());
                        }else{
                            form.setCityId(org.getId());
                        }
                    }else{
                        form.setCityId(org.getId());
                    }
                }
            }
        }
        form.setApplyTime(form.getApplyTime());
        form.setDeleteStatus(form.getDeleteStatus());// 正常
        form.setStatus(form.getStatus());// 网报待确认
        form.setLastModify(form.getLastModify());
        form.setLastModifier(form.getIdNo());
        form.setTriggerTime(form.getTriggerTime());
        form.setIp(form.getIp());
        form.setStatusMemo(form.getStatusMemo());
        form.setDataFrom(form.getDataFrom());
        form.setExam(form.getExam());
        form.setPassword(form.getPassword()); // 使用 MD5 编码密码
        form.setScoreCertNo(form.getScoreCertNo());
        registrationMapper.insertRequestReg(form);
    }

    public void updateScoreStatus(RegistrationForm form){
        Score score = commonMapper.findByScoreId(form.getScoreId());
        if(score != null){
            commonMapper.updateScoreStatus(form.getCertBatchId(),SignUpConstants.STATUS_USED,form.getScoreId());
        }
    }

    public void saveResum(RegistrationForm form){
        //首先给registration设置值
        String resumInfo = form.getResumInfo();
        if(!resumInfo.isEmpty()){
            String[] resums = resumInfo.split(",");
            for(int i = 0; i < resums.length; i++){
                if(!resums[i].isEmpty()){
                    String[] item = resums[i].split("-");
                    Resume resume = new Resume();
                    resume.setRegId(form.getRegId());
                    resume.setStartDate(item[0]);
                    resume.setEndDate(item[1]);
                    resume.setWorkUnit(item[2]);
                    resume.setJob(item[3]);
                    resume.setCertifier(item[4]);
                    commonMapper.insertResume(resume);
                }
            }
        }
    }

    /**
     * 保存 Enroll 的图片
     * @param form
     */
    public void saveExamPhoto(RegistrationForm form) {
        String tempDir = config.getString("uploadTemp"); // 图片的临时目录
        String tempName = form.getTmpPhoto();
        String tempPhotoPath = tempDir + File.separator + tempName; // 临时图片路径
        String photoDir = config.getString("uploadRegPhotoDir"); // 图片的最终目录
        String photoPath = generateExamPhotoPath(form.getRegId(), photoDir);

        try {
            FileUtils.moveFile(new File(tempPhotoPath), new File(photoPath));
        } catch (IOException e) {
            logger.warn("移动图片失败: {}", e.getMessage());
        }
        try {
            FileUtils.deleteQuietly(new File(tempPhotoPath));
        } catch (Exception e) {
            logger.warn("临时目录图片删除失败: {}", e.getMessage());
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
    public String generateExamPhotoPath(long regId, String dir) {
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
        userPortalLog.setType(UserPortalLog.RENDING_SUBMIT);
        userPortalLog.setBrowserContent(BrowserUtils.getBrowserContent(request));
        userPortalLog.setBrowserName(BrowserUtils.getBrowserName(request));

        commonMapper.insertUserPortalLog(userPortalLog);
    }

    public Result<?> validateParams(RegistrationForm form, BindingResult result) {
        StringBuffer sb = new StringBuffer();
        for (FieldError error : result.getFieldErrors()) {
            sb.append(error.getField() + " : " + error.getDefaultMessage() + "\n");
        }
        if (result.hasErrors()) {
            return new Result(false, sb.toString());
        }
        try {
            // 例如 2012-22-12-12 也是能够被转换为日期，所以最后需要再格式化回去为合法的日期
            Date graduationTime = DateUtils.parseDate(form.getGraduaTime(), DATE_FORMAT);
            Date birthday = DateUtils.parseDate(form.getBirthDay(), DATE_FORMAT);
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
