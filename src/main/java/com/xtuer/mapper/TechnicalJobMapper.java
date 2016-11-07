package com.xtuer.mapper;

import com.xtuer.dto.TechnicalJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 专业技术职务
 *
 * Created by microacup on 2016/10/19.
 */
public interface TechnicalJobMapper {
    List<TechnicalJob> findRoots();
    List<TechnicalJob> findByParent(@Param("parentId") int parentId);
    List<TechnicalJob> findByName(@Param("name") String name);
}
