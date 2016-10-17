package com.xtuer.controller;

import com.xtuer.bean.Result;
import com.xtuer.dto.CertType;
import com.xtuer.dto.Province;
import com.xtuer.mapper.CertTypeMapper;
import com.xtuer.mapper.OrganizationMapper;
import com.xtuer.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.xtuer.util.RedisUtils.get;

@Controller
public class SignUpController {
    @Autowired
    private CertTypeMapper certTypeMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping(UriViewConstants.REST_CERT_TYPE)
    @ResponseBody
    public Result<List<CertType>> getCertTypes() {
        List<CertType> types = get(List.class, redisTemplate, "certTypes", () -> certTypeMapper.getAll());
        return Result.ok(types);
    }


    @GetMapping(UriViewConstants.PROVINCES)
    @ResponseBody
    public Result<List<Province>> getProvinces() {
        List<Province> provinces = RedisUtils.get(List.class, redisTemplate, "provinces", () -> organizationMapper.listProvinces());
        return Result.ok(provinces);
    }



}
