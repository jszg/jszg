package com.xtuer.mapper;

import com.xtuer.dto.Major;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 所学专业
 *
 * Created by microacup on 2016/10/19.
 */
public interface MajorMapper {

    // parent:如果是注册的
    List<Major> findRoot();

    // childen:如果是注册
    List<Major> findByParentIdStatus1(@Param("parentId") int parentId);

    List<Major> findByParentId(@Param("parentId") int parentId);

    // parent:如果是认定的
    List<Major> findByCertTypeIdAndEduLevelId(@Param("certTypeId") int certTypeId, @Param("eduLevelId") int eduLevelId);

    // childen:如果是认定
    List<Major> findByParentIdAndProvince(@Param("parentId") int parentId, @Param("provinceId") int provinceId);

    //根据专业名称查询专业
    List<Major> findByName(@Param("name") String name);

}
