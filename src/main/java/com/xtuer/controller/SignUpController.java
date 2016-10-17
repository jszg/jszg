package com.xtuer.controller;

import com.xtuer.bean.Result;
import com.xtuer.dto.CertType;
import com.xtuer.mapper.CertTypeMapper;
import com.xtuer.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SignUpController {
    @Autowired
    private CertTypeMapper certTypeMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping(UriViewConstants.REST_CERT_TYPE)
    @ResponseBody
    public Result<List<CertType>> getCertTypes() {
        List<CertType> types = RedisUtils.get(List.class, redisTemplate, "certType", () -> certTypeMapper.getAll());
        return Result.ok().setData(types);
    }


}
