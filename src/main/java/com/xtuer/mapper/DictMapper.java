package com.xtuer.mapper;

import com.xtuer.dto.Dict;
import com.xtuer.dto.TeachGrade;
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

    List<TeachGrade> findTeaGrades();

    List<Dict> findEduLevels(@Param("ct_id") int certTypeId);

    List<Dict> findAcademicDegrees(@Param("ct_id") int certTypeId, @Param("eduLevel") int eduLevel);

    //根据字典类型和编码查询
    Dict findByTypeAndCode(@Param("dtId") int dtId, @Param("code") int code);
}
