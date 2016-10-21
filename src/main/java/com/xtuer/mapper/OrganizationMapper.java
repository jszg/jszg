package com.xtuer.mapper;

import com.xtuer.dto.Organization;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组织机构
 *
 * Created by microacup on 2016/10/17.
 */
public interface OrganizationMapper {

    // 认定机构 1-5
    List<Organization> findByCertTypeAndCity(@Param("city") Integer city, @Param("certTypeId") Integer certTypeId);

    // 认定机构 6-7
    List<Organization> findByCertTypeAndProvince(@Param("province") Integer province, @Param("certTypeId") Integer certTypeId);

    List<Organization> findByOrgType(@Param("orgType") int orgType);

    // 查询OrgType==4的Organization
    List<Organization> findByOrgTypeEq4();

    List<Organization> findByParentId(@Param("parentId") int parentId);

    // 注册机构 ct_code==7
    List<Organization> findByOrgId(@Param("orgId") int orgId);

    // 注册机构 ct_code!=7 && is_province_city
    List<Organization> findByProvinceCity(@Param("provinceCityId") int provinceCityId);

    // 注册机构 ct_code!=7 && !is_province_city
    List<Organization> findByCity(@Param("cityId") int cityId);


}
