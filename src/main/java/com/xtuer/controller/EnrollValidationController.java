package com.xtuer.controller;

import com.xtuer.bean.Result;
import com.xtuer.constant.UriView;
import com.xtuer.dto.OrgBatch;
import com.xtuer.dto.OrgBatchTime;
import com.xtuer.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 注册使用的验证类
 */
@Controller
public class EnrollValidationController {
    /**
     * 验证注册的注册机构
     *
     * @param orgId 注册机构的 id
     * @return Result 对象
     */
    @GetMapping(UriView.REST_ENROLL_ORG_VALIDATION)
    @ResponseBody
    public Result<?> validateOrganization(@PathVariable int orgId) {
        List<OrgBatch> orgBatchs = commonMapper.findOrgBatch(orgId);

        if (orgBatchs.isEmpty()) {
            return new Result(false, "该机构目前未开展注册工作，请与该机构联系，了解其注册工作的时间安排");
        }

        OrgBatch orgBatch = orgBatchs.get(0);
        if (orgBatch != null && orgBatch.getProvinceBatch() != null) {
            return new Result(false, "该机构当前注册工作已经结束");
        }

        if (orgBatch != null && orgBatch.getJoinIn() != null && !orgBatch.getJoinIn()) {
            return new Result(false, "该机构目前未开展注册工作，请与该机构联系，了解其注册工作的时间安排");
        }

        if (orgBatch.getOpen() != null && !orgBatch.getOpen()) {
            return new Result(false, "该机构注册工作目前未安排网上采集信息的时间，请与该机构联系，了解其注册工作的时间安排");
        }

        // 网报时间
        List<OrgBatchTime> orgBatchTimes = commonMapper.findOrgBatchTime(orgId);
        if (!orgBatchTimes.isEmpty()) {
            StringBuffer buffer = new StringBuffer("该机构注册工作网上采集信息的时间段为: ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy 年 MM 月 dd 日");

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
            }

            return new Result(false, buffer.toString());
        }

        return Result.ok();
    }

    @Autowired
    private CommonMapper commonMapper;
}
