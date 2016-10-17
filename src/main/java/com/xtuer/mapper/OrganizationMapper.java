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

    List<Organization> findByCertTypeAndCity(@Param("city") Integer city, @Param("certTypeId") Integer certTypeId);

    List<Organization> listByCertTypeAndProvince(@Param("province") Integer province, @Param("certTypeId") Integer certTypeId);

}
