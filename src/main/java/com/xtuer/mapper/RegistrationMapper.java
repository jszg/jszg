package com.xtuer.mapper;

import com.xtuer.bean.EnrollmentForm;
import com.xtuer.bean.RegistrationForm;
import org.apache.ibatis.annotations.Param;

public interface RegistrationMapper {
    public RegistrationForm deleteByRegisterId(@Param("registerId") int registerId);
    //inRegistration时修改认定的注册计划
    public void updateEnrollProBatch(@Param("provinceBatchId") int provinceBatchId,@Param("registerId") int regId);
    long insertRegistration(RegistrationForm form);
}
