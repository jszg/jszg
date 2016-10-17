package com.xtuer.mapper;

import com.xtuer.dto.City;

import java.util.List;

/**
 * 城市
 *
 * Created by microacup on 2016/10/17.
 */
public interface CityMapper {
    List<City> findByParentId(int parentId);
}
