package com.xtuer.mapper;

import com.xtuer.dto.Organization;

import java.util.List;

/**
 * 组织机构
 *
 * Created by microacup on 2016/10/17.
 */
public interface OrganizationMapper {

    List<Organization> listByProvince();

}
