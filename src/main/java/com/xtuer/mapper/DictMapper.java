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

    List<Dict> findByDictTypeStatus(@Param("dt_id") int dictTypeId);

    List<TeachGrade> findTeaGrades();

    List<Dict> findTeaGradesByStatus();

    List<Dict> findEduLevels(@Param("ct_id") int certTypeId);

    //根据字典类型和编码查询
    Dict findByTypeAndCode(@Param("dtId") int dtId, @Param("code") int code);

    //认定报名根据资格种类和最高学历选择最高学位
    List<Dict> findDegreessByCertTypeIdAndEduLevelId(@Param("certTypeId") int certTypeId, @Param("eduLevelId") int eduLevelId);
}
