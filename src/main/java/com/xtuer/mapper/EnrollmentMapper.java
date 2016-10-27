package com.xtuer.mapper;

import com.xtuer.bean.EnrollmentForm;
import org.apache.ibatis.annotations.Param;

public interface EnrollmentMapper {
    public EnrollmentForm findByRegisterId(@Param("registerId") int registerId);
}
