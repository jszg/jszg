package com.xtuer.mapper;

import com.xtuer.bean.EnrollmentForm;
import com.xtuer.bean.RegistrationForm;
import org.apache.ibatis.annotations.Param;

public interface RegistrationMapper {
    public RegistrationForm deleteByRegisterId(@Param("registerId") int registerId);
}
