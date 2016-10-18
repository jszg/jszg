package com.xtuer.controller;

import com.xtuer.bean.Result;
import com.xtuer.constant.RedisKey;
import com.xtuer.constant.UriView;
import com.xtuer.dto.CertType;
import com.xtuer.dto.City;
import com.xtuer.dto.Dict;
import com.xtuer.dto.Organization;
import com.xtuer.dto.Province;
import com.xtuer.dto.Subject;
import com.xtuer.mapper.CertTypeMapper;
import com.xtuer.mapper.CityMapper;
import com.xtuer.mapper.DictMapper;
import com.xtuer.mapper.OrganizationMapper;
import com.xtuer.mapper.ProvinceMapper;
import com.xtuer.mapper.SubjectMapper;
import com.xtuer.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@Controller
public class SignUpController {
    // 所有资格种类
    @GetMapping(UriView.REST_CERT_TYPE)
    @ResponseBody
    public Result<List<CertType>> getCertTypes() {
        List<CertType> types = redisUtils.get(List.class, RedisKey.CERT_TYPES, () -> certTypeMapper.findAll());
        return Result.ok(types);
    }

    // 所有的省
    @GetMapping(UriView.REST_PROVINCES)
    @ResponseBody
    public Result<List<Province>> getProvinces() {
        List<Province> provinces = redisUtils.get(List.class, RedisKey.PROVINCES, () -> provinceMapper.findAll());
        return Result.ok(provinces);
    }

    // 省下面所有的市
    @GetMapping(UriView.REST_CITIES_BY_PROVINCE)
    @ResponseBody
    public Result<List<City>> getCities(@PathVariable("provinceId") Integer id) {
        String key = String.format(RedisKey.CITIES, id);
        List<City> cities = redisUtils.get(List.class, key, () -> cityMapper.findByParentId(id));

        return Result.ok(cities);
    }

    // 市或省下面的所有的认定机构
    // 1-5 查市下面的所有的认定机构
    // 6-7 查省下面的所有的认定机构
    @GetMapping(UriView.REST_ORGS_BY_CITY_AND_CERT_TYPE)
    @ResponseBody
    public Result<List<Organization>> getOrgByCity(@PathVariable("cityId") Integer cityId, @PathVariable("certTypeId") Integer certTypeId) {
        List<Organization> orgs = Collections.emptyList();
        String key = String.format(RedisKey.ORGS, cityId, certTypeId);

        if (certTypeId >= 1 && certTypeId <= 5) {
            // 1-5 查市
            orgs = redisUtils.get(List.class, key, () -> organizationMapper.findByCertTypeAndCity(cityId, certTypeId));
        } else if (certTypeId >= 6 && certTypeId <= 7) {
            // 6-7 查省
            orgs = redisUtils.get(List.class, key, () -> organizationMapper.listByCertTypeAndProvince(cityId, certTypeId));
        }

        return Result.ok(orgs);
    }

    // 省下面的第一级任教学科
    @GetMapping(UriView.REST_SUBJECTS_ROOT)
    @ResponseBody
    public Result<List<Subject>> getRootSubjects(@PathVariable("provinceId") int provinceId, @PathVariable("certTypeId") int certTypeId) {
        String key = String.format(RedisKey.SUBJECTS_ROOT, provinceId, certTypeId);
        List<Subject> subjects = redisUtils.get(List.class, key, () -> subjectMapper.findRoots(provinceId, certTypeId));

        return Result.ok(subjects);
    }

    // 省下面指定父节点的任教学科
    @GetMapping(UriView.REST_SUBJECTS_CHILDREN)
    @ResponseBody
    public Result<List<Subject>> getChildrenSubjects(@PathVariable("provinceId") int provinceId, @PathVariable("parentId") int parentId) {
        String key = String.format(RedisKey.SUBJECTS_CHILDREN, provinceId, parentId);
        List<Subject> subjects = redisUtils.get(List.class, key, () -> subjectMapper.findByParent(parentId, provinceId));

        return Result.ok(subjects);
    }

    // 按照字典类型查询字典数据
    @GetMapping(UriView.REST_DICTS_BY_DICTTYPE)
    @ResponseBody
    public Result<List<Dict>> getDicts(@PathVariable("dictTypeId") int dictTypeId) {
        String key = String.format(RedisKey.DICTS, dictTypeId);
        List<Dict> dicts = redisUtils.get(List.class, key, () -> dictMapper.findByDictType(dictTypeId));
        return Result.ok(dicts);
    }

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CertTypeMapper certTypeMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private DictMapper dictMapper;
}
