package com.xtuer.mapper;

import com.xtuer.bean.EnrollmentForm;
import org.apache.ibatis.annotations.Param;

public interface EnrollmentMapper {
    int insertEnrollment(EnrollmentForm form);
}
