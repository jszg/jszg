package com.xtuer.controller;

import com.xtuer.bean.Result;
import com.xtuer.constant.SignUpConstants;
import com.xtuer.constant.UriView;
import com.xtuer.dto.*;
import com.xtuer.mapper.CommonMapper;
import com.xtuer.util.CommonUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 注册使用的验证类
 */
@Controller
public class EnrollmentValidationController {
    /**
     * 验证注册的注册机构
     *
     * @param orgId 注册机构的 id
     * @return Result 对象
     */
    @GetMapping(UriView.REST_ENROLL_ORG_VALIDATION)
    @ResponseBody
    public Result<?> validateOrganization(@PathVariable int orgId) {
        List<OrgBatch> orgBatchs = commonMapper.findOrgBatch(orgId,SignUpConstants.TYPE_ENROLL); // 查询注册机构注册计划

        if (orgBatchs.isEmpty()) {
            return new Result(false, "该机构目前未开展注册工作，请与该机构联系，了解其注册工作的时间安排");
        }

        OrgBatch orgBatch = orgBatchs.get(0);
        if (orgBatch != null && orgBatch.getProvinceBatch() != null && orgBatch.getStatus() != null && orgBatch.getStatus() >= 4) {
            return new Result(false, "该机构当前注册工作已经结束");
        }

        if (orgBatch != null && orgBatch.getJoinIn() != null && !orgBatch.getJoinIn()) {
            return new Result(false, "该机构目前未开展注册工作，请与该机构联系，了解其注册工作的时间安排");
        }

            if (orgBatch.getIsOpen() != null && !orgBatch.getIsOpen()) {
            return new Result(false, "该机构注册工作目前未安排网上采集信息的时间，请与该机构联系，了解其注册工作的时间安排");
        }

        List<OrgBatchTime> orgBatchTimes = commonMapper.findOrgBatchTime(orgId,SignUpConstants.TYPE_ENROLL); // 查询网报时间段

        if (orgBatchTimes.isEmpty()) {
            orgBatchTimes = commonMapper.findOrgBatchTimeByOrgBatchId(orgBatch.getOrgBatchId());

            // 没有则返回错误
            if (orgBatchTimes.isEmpty()) {
                return new Result(false, "该机构目前未开展注册工作，请与该机构联系，了解其注册工作的时间安排");
            }

            // 如果有网报时间，则判断时间是否合法
            StringBuffer buffer = new StringBuffer("该机构注册工作网上采集信息的时间段为: ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
            boolean success = true;

            for (OrgBatchTime batchTime : orgBatchTimes) {
                String prependMsg = "";

                if (batchTime.getValidBeginDate().after(new Date())) {
                    prependMsg = "还未到网报时间";
                }
                if (batchTime.getValidEndDate().before(new Date())) {
                    prependMsg = "网报时间已截止";
                }

                buffer.append(String.format("%s: %s 到 %s; ", prependMsg,
                        dateFormat.format(batchTime.getValidBeginDate()),
                        dateFormat.format(batchTime.getValidEndDate())));

                success = false;
            }

            return new Result(success, buffer.toString());
        }

        return Result.ok(orgBatchTimes.get(0));
    }

    // 注册验证 Step3
    @GetMapping(UriView.REST_ENROLL_STEP3)
    @ResponseBody
    public Result<?> enrollStep3(@RequestParam String idNo, @RequestParam String certNo) {
        List<Limitation> limits = commonMapper.findLimitation(idNo, certNo);

        if (!limits.isEmpty() && limits.get(0).getStatus() == SignUpConstants.S_REVIEWED) {
            Limitation limitation = limits.get(0);
            Integer limitationType = limitation.getType();

            if (limitationType == null) {
                return new Result(false, "该证书受到限制，不允许注册");
            } else if (limitationType == SignUpConstants.T_CANCEL1 || limitationType == SignUpConstants.T_CANCEL2) {
                return new Result(false, "该教师资格已被依法撤销");
            } else if (limitationType == SignUpConstants.T_LOSE) {
                return new Result(false, "该教师资格已被依法注销");
            } else if (limitationType == SignUpConstants.T_CHEAT) {
                return new Result(false, "您的教师资格无效");
            }
        }

        HistoryValid historyValid = null;

        List<HistoryValid> historyValids = commonMapper.findHistoryValid(idNo, certNo);
        if (!historyValids.isEmpty()) {
            historyValid = historyValids.get(0);
        }

        EnrollHistory enrollHistory = null;
        String provinceCode = null;
        List<EnrollHistory> enrollHistories = commonMapper.findEnrollHistory(idNo, certNo);

        if (!enrollHistories.isEmpty()) {
            enrollHistory = enrollHistories.get(0);
            if(enrollHistory.getEnrollTime() == null){
                return new Result(false, "注册历史数据存在异常，请联系网站工作人员");
            }

            Calendar expiredTime = Calendar.getInstance();
            expiredTime.setTime(enrollHistory.getEnrollTime());
            if(enrollHistory.getJudgmentStatus()!=null){
                switch (enrollHistory.getJudgmentStatus()) {
                    case SignUpConstants.STATUS_UN_DO:
                        expiredTime.add(Calendar.YEAR, SignUpConstants.UN_DO_DURATION);
                        if (expiredTime.getTime().after(new Date())) {
                            String errorMsg = "该证书于" + DateFormatUtils.format(enrollHistory.getEnrollTime(), "yyyy年M月d日")
                                    + "已被撤销注册，" + SignUpConstants.UN_DO_DURATION + "年内不能再注册。不能继续下一步!";
                            return new Result(false, errorMsg);
                        }
                        break;
                    // STATUS_QUALIFIED = 19;// ***注册合格
                    case SignUpConstants.STATUS_QUALIFIED:
                        //三-3、“注册合格”且与上次注册日期间隔短于58个月
                        expiredTime.add(Calendar.MONTH, SignUpConstants.ENROLL_DURATION);
                        if (expiredTime.getTime().after(new Date())) {
                            String errorMsg = "该证书已于" + DateFormatUtils.format(enrollHistory.getEnrollTime(), "yyyy年M月d日")
                                    + "注册，5年内无需再注册。";
                            return new Result(false, errorMsg);
                        } else {
                            if (null == historyValid) {
                                return new Result(false, "证书数据存在异常，请联系网站工作人员");
                            }
                        }

                        break;
                    case SignUpConstants.STATUS_CERT_LATER:
                        if (enrollHistory.getCertNo() != null && historyValid == null) {
                            return new Result(false, "证书数据存在异常，请联系网站工作人员");
                        }

                        break;
                }
            }
        }

        List<Enrollment> enrollments = commonMapper.findEnrollment(idNo, certNo);
        if (!enrollments.isEmpty()) {
            return new Result(false, "您已经填写了申报信息，请直接登陆查看或修改申报信息；");
        }

        Enrollment enrollment = new Enrollment();
        Registration registration = new Registration();

        enrollment.setCertNo(certNo);
        enrollment.setIdNo(idNo);

        if (historyValid == null ) {
            enrollment.setInHistory(false);
            List<Registration> registrations = commonMapper.findRegistration(idNo, certNo);
            if (!registrations.isEmpty()) {
                registration = registrations.get(0);
                enrollment.setInRegistration(true);
            }else{
                int year = 0;
                if (certNo.length() > 15) {
                    year = Integer.parseInt(certNo.substring(0, 4));
                }else{
                    year = 1900 + Integer.parseInt(certNo.substring(0, 2));
                }

                if (year >= 2012 || year < 1996) {
                    return new Result(false, "无此教师资格,请与发证机关确认");
                }

                if(year>=2008 && year<2012) {
                    provinceCode = certNo.substring(4, 6);//这个地方是省份编码,判断是否是广西省,第四步用到
                }
                enrollment.setInRegistration(Boolean.FALSE);
            }
            enrollment.setEnrollNum(1);
            registration.setIdNo(idNo);
            registration.setCertNo(certNo);
        } else {
            if(historyValid.getDeleteStatus() == SignUpConstants.DELETE_STATUS_FORBID){
                return new Result(false, "该数据已受限，不能进行定期注册报名!");
            }

            if(historyValid.getDeleteStatus() == SignUpConstants.DELETE_STATUS_DELETE){
                return new Result(false, "该证书不存在，请您仔细核对信息!");
            }

            // 此时应该返回 historyValid 的所有信息在第四步上端显示;
            registration.setId(historyValid.getId());
            registration.setCertType(historyValid.getCertType());
            registration.setName(historyValid.getName());
            registration.setCertNo(certNo);
            registration.setIdNo(idNo);
            registration.setBirthday(historyValid.getBirthday());
            registration.setNationName(historyValid.getNationName());
            registration.setOrgName(historyValid.getOrgName());
            registration.setSexName(historyValid.getSexName());
            registration.setIdTypeName(historyValid.getIdTypeName());
            registration.setSubjectName(historyValid.getSubjectName());
            registration.setCertAssign(historyValid.getCertAssign());

            enrollment.setInHistory(true);
            if(enrollHistory == null){
                enrollment.setEnrollNum(1);
            }else{
                if (enrollHistory.getJudgmentStatus() == SignUpConstants.STATUS_QUALIFIED) {
                    enrollment.setEnrollNum(enrollHistory.getEnrollNum() + 1);
                } else {
                    enrollment.setEnrollNum(enrollHistory.getEnrollNum());
                }
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("enrollment", enrollment);
        map.put("historyValid", historyValid);
        map.put("registration", registration);
        map.put("provinceCode", provinceCode);
        return Result.ok(map);
    }

    @Autowired
    private CommonMapper commonMapper;
}
