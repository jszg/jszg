package com.xtuer.mapper;

import com.xtuer.bean.EnrollmentForm;
import org.apache.ibatis.annotations.Param;

public interface EnrollmentMapper {
    EnrollmentForm findByRegisterId(@Param("registerId") int registerId);
    int insertEnrollment(EnrollmentForm form);
}
