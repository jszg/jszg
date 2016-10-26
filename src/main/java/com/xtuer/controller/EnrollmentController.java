package com.xtuer.controller;

import com.xtuer.bean.EnrollmentForm;
import com.xtuer.bean.Result;
import com.xtuer.constant.UriView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 注册的 Controller
 */
@Controller
public class EnrollmentController {
    @PostMapping(UriView.URI_ENROLL_SUBMIT)
    @ResponseBody
    public Result<?> submitEnroll(@RequestBody EnrollmentForm enroll) {

        return Result.ok();
    }
}
