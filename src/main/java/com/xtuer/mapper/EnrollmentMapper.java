package com.xtuer.mapper;

import com.xtuer.bean.EnrollmentForm;
import org.apache.ibatis.annotations.Param;

public interface EnrollmentMapper {
    //根据认定id查询认定历史表
    EnrollmentForm findHistoryValidByRegisterId(@Param("registerId") int registerId);
    int insertEnrollment(EnrollmentForm form);
    //根据认定Id查询认定正式表
    EnrollmentForm findRegistrationByRegisterId(@Param("registerId") int registerId);
}
