package com.xtuer.mapper;

import com.xtuer.dto.College;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 毕业学校
 *
 * Created by microacup on 2016/10/19.
 */
public interface CollegeMapper {
    List<College> findAll();
    List<College> findByProvinceId(@Param("provinceId") int provinceId);
    List<College> findByName(@Param("name") String name);
}
