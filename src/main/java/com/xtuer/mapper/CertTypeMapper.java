package com.xtuer.mapper;

import com.xtuer.dto.CertType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CertTypeMapper {
    List<CertType> findAll();
    List<CertType> findByTeachGrade(@Param("teachGrade") int teachGrade);
}
