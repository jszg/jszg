package com.xtuer.mapper;

import com.xtuer.bean.EnrollmentForm;
import com.xtuer.bean.RegistrationForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RegistrationMapper {
    public RegistrationForm deleteByRegisterId(@Param("registerId") int registerId);
    //inRegistration时修改认定的注册计划
    public void updateEnrollProBatch(@Param("provinceBatchId") int provinceBatchId,@Param("registerId") long regId);
    //注册报名中保存认定数据
    long insertRegistration(RegistrationForm form);

    //非统考认定第六步验证是否在认定表中
    List<RegistrationForm> findByNameAndIdNo(@Param("name") String name, @Param("idNo") String idNo);

    //认定报名保存数据
    long insertRequestReg(RegistrationForm form);
}
