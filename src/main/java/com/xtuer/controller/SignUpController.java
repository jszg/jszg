package com.xtuer.controller;

import com.xtuer.bean.Result;
import com.xtuer.dto.CertType;
import com.xtuer.dto.City;
import com.xtuer.dto.Organization;
import com.xtuer.dto.Province;
import com.xtuer.mapper.CertTypeMapper;
import com.xtuer.mapper.CityMapper;
import com.xtuer.mapper.OrganizationMapper;
import com.xtuer.mapper.ProvinceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

import static com.xtuer.util.RedisUtils.get;

@Controller
public class SignUpController {
    @Autowired
    private CertTypeMapper certTypeMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping(UriViewConstants.REST_CERT_TYPE)
    @ResponseBody
    public Result<List<CertType>> getCertTypes() {
        List<CertType> types = get(List.class, redisTemplate, "certTypes", () -> certTypeMapper.findAll());
        return Result.ok(types);
    }


    @GetMapping(UriViewConstants.REST_PROVINCES)
    @ResponseBody
    public Result<List<Province>> getProvinces() {
        List<Province> provinces = get(List.class, redisTemplate, "provinces", () -> provinceMapper.findAll());
        return Result.ok(provinces);
    }

    @GetMapping(UriViewConstants.REST_CITIES_BY_PARENT)
    @ResponseBody
    public Result<List<City>> getCities(@PathVariable("parentid") Integer id) {
        List<City> cities = get(List.class, redisTemplate, "cities_" + id, () -> cityMapper.findByParentId(id));
        return Result.ok(cities);
    }

    @GetMapping(UriViewConstants.REST_ORGS_BY_CT_AND_CITY)
    @ResponseBody
    public Result<List<Organization>> getOrgByCity(@PathVariable("city") Integer city, @PathVariable("certTypeId") Integer certTypeId) {
        List<Organization> orgs = Collections.emptyList();

        // 1-5 查市
        if (certTypeId.intValue() >= 1 && certTypeId.intValue() <= 5) {
            orgs = get(List.class, redisTemplate, "org_" + city + "_c_" + certTypeId,
                    () -> organizationMapper.findByCertTypeAndCity(city, certTypeId));
        } else if (certTypeId.intValue() >= 6 && certTypeId.intValue() <= 7) { // 6-7 查省
            orgs = get(List.class, redisTemplate, "org_" + city + "_c_" + certTypeId,
                    () -> organizationMapper.listByCertTypeAndProvince(city, certTypeId));
        }
        return Result.ok(orgs);
    }

}
