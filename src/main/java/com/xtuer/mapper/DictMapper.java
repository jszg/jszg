package com.xtuer.mapper;

import com.xtuer.dto.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典表
 *
 * table: T_DICT AND T_DICT_TYPE
 * Created by microacup on 2016/10/18.
 */
public interface DictMapper {

    List<Dict> findByDictType(@Param("dt_id") int dictTypeId);

    List<Dict> findTeaGrades();

    List<Dict> findEduLevels(@Param("ct_id") int certTypeId);

    List<Dict> findAcademicDegrees(@Param("ct_id") int certTypeId, @Param("eduLevel") int eduLevel);

}
