package com.xtuer.controller;

import com.xtuer.bean.RegistrationForm;
import com.xtuer.bean.Result;
import com.xtuer.constant.SignUpConstants;
import com.xtuer.constant.UriView;
import com.xtuer.dto.*;
import com.xtuer.mapper.CommonMapper;
import com.xtuer.mapper.RegistrationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 认定使用的验证类
 */
@Controller
public class ExamValidationController {

    // 统考验证 Step3
    @GetMapping(UriView.REST_EXAM_STEP3)
    @ResponseBody
    public Result<?> enrollStep6(@RequestParam String name, @RequestParam String idNo, @RequestParam String scoreCertNo) throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        if(name == null){
            return Result.ok(null);
        }
        try {
            name = java.net.URLDecoder.decode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<Limitation> limits = commonMapper.findLimitationByNameAndIdNo(name, idNo);
        if (!limits.isEmpty()) {
            Limitation limit = limits.get(0);
            if (limit.getDueTime() == null)
                return new Result(false, "您已丧失教师资格，不能再申请教师资格！");
            else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
                return new Result(false, "您因" + limit.getReason() + "被限制申请教师资格，限制结束时间为" + sdf.format(limit.getDueTime()));
            }
        }else{
            List<RegistrationForm> tmpList = registrationMapper.findByNameAndIdNo(name, idNo);
            if(!tmpList.isEmpty()){
                return new Result(false, "您已经填写了申报信息，请直接登录查看或修改申报信息！");
            }
        }

        Calendar c = Calendar.getInstance();
        List<HistoryValid> hvList = commonMapper.findByUserYear(name, idNo, c.get(Calendar.YEAR));
        Score score = null;
        if(!hvList.isEmpty() && hvList.get(0) != null){
            return new Result(false, "您在本年度内已经申请了" + hvList.get(0).getCertType() + "，同一自然年内不允许申请两种教师资格");
        }else{
            List<Score> scoreList = commonMapper.findByUserExamNo(name, idNo, scoreCertNo);
            if(!scoreList.isEmpty()){
                score = scoreList.get(0);
            }
            if(score == null){
                return new Result(false, "您没有获得合格的统考成绩，无法通过该途径申报，如确认输入无误，请与参加统考时的考试机构联系!");
            }else {
                if(score.getExpiredTime() != null && score.getExpiredTime().before(new Date())){
                    return new Result(false, "统考成绩截止日期已过!");
                }
            }
        }
        List<HistoryValid> list = commonMapper.checkHistoryExists(name, idNo, score.getCertType(),score.getSubject());
        if(!list.isEmpty()){
            return new Result(false, "您已经申报过此种教师资格，不允许再次申报!");
        }
        map.put("score", score);
        return Result.ok(map);
    }


    /**
     * 验证统考的认定机构
     *
     * @param orgId 认定机构的 id
     *@param certTypeId 资格种类的 id
     * @return Result 对象
     */
    @GetMapping(UriView.REST_EXAM_ORG_VALIDATION)
    @ResponseBody
    public Result<?> validateRequestOrganization(@PathVariable int orgId, @PathVariable int certTypeId) {
        Map<String, Object> map = new HashMap<>();
        //[1]首先判断认定机构是否有认定所选资格种类的权限
        List<OrgCertType> octs = commonMapper.findOrgCertTypeByOrgId(orgId);
        Set<Integer> ctIds = new HashSet<Integer>();
        for (OrgCertType oct : octs) {
            ctIds.add(oct.getCertTypeId());
        }
        if (!ctIds.contains(certTypeId)) {
            return new Result(false, "该机构没有认定此种教师资格的权限，请与该机构联系。在河北省申请认定幼儿园、小学、初级中学教师资格的申请人，认定机构选择市级教育局。");
        }
        //[2] 所选机构是否已经开展工作 是否有认定的功能
        List<OrgBatch> orgBatchs = commonMapper.findOrgBatch(orgId,SignUpConstants.TYPE_CERT); // 查询非统考认定机构认定计划
        if (orgBatchs.isEmpty()) {
            return new Result(false, "该机构目前未开展注册工作，请与该机构联系，了解其注册工作的时间安排");
        }
        Date now = new Date();
        OrgBatch ob = orgBatchs.get(0);
        if (ob == null || !ob.getJoinIn() || ob.getBeginDate().after(now) || ob.getEndDate().before(now)) {
            return new Result(false, "该机构目前未开展认定工作，请与该机构联系，了解其认定工作的时间安排");
        } else if (!ob.getIsOpen()) {
            return new Result(false, "该机构不组织网上采集认定信息，请与该机构联系，了解其认定工作的具体安排");
        }

        //[3] 所选机构开展工作的时间是否已经到
        List<OrgBatchTime> orgBatchTimes = commonMapper.findOrgBatchTime(orgId,SignUpConstants.TYPE_CERT); // 查询网报时间段
        if (orgBatchTimes.isEmpty()) {
            orgBatchTimes = commonMapper.findOrgBatchTimeByOrgBatchId(ob.getOrgBatchId());

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
                if (batchTime.getValidBeginDate() != null && batchTime.getValidEndDate() != null) {
                    if (batchTime.getValidBeginDate().after(new Date())) {
                        prependMsg = "很抱歉，该机构的网上报名时间未到，";
                    }
                    if (batchTime.getValidEndDate().before(new Date())) {
                        prependMsg = "很抱歉，该机构的网上报名时间已截止，";
                    }
                }
                buffer.append(String.format("%s: %s 到 %s; ", prependMsg,
                        dateFormat.format(batchTime.getValidBeginDate()),
                        dateFormat.format(batchTime.getValidEndDate())));

                success = false;
            }

            return new Result(success, buffer.toString());
        }else{
            map.put("certBatchId", ob.getCertBatchId());
        }
        map.put("orgBatchTimes", orgBatchTimes.get(0));

        return Result.ok(map);
    }



    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private RegistrationMapper registrationMapper;
}
