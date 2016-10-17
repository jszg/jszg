package com.xtuer.mapper;

import com.xtuer.dto.Subject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任教学科
 *
 * Created by microacup on 2016/10/17.
 */
public interface SubjectMapper {
    List<Subject> findRoots(@Param("province") int province, @Param("certTypeId") int certTypeId);
    List<Subject> findByParent(@Param("parent") int parent, @Param("province") int province);
}