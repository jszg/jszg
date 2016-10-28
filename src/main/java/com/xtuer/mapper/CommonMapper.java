package com.xtuer.mapper;

import com.xtuer.bean.UserPortalLog;
import com.xtuer.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 共用Mappper
 *
 * Created by microacup on 2016/10/24.
 */
public interface CommonMapper {
    List<Limitation> findLimitation(@Param("idNo") String idNo, @Param("certNo") String certNo);
    List<EnrollHistory> findEnrollHistory(@Param("idno") String idno, @Param("certno") String certno);
    List<HistoryValid> findHistoryValid(@Param("idno") String idno, @Param("certno") String certno);
    HistoryValid findHistoryValidById(@Param("regId") int regId);
    List<Registration> findRegistration(@Param("idno") String idno, @Param("certno") String certno);
    Registration findRegistrationById(@Param("regId") int regId);
    List<Enrollment> findEnrollment(@Param("idno") String idno, @Param("certno") String certno);
    List<Enrollment> findEnrollmentStatus0(@Param("idno") String idno, @Param("certno") String certno);
    List<OrgBatch> findOrgBatch(@Param("orgId") int orgId);
    List<OrgBatchTime> findOrgBatchTime(@Param("orgId") int orgId);

    // 插入UserPortalLog
    void insertUserPortalLog(UserPortalLog log);
    List<OrgBatchTime> findOrgBatchTimeByOrgBatchId(@Param("orgBatchId") int orgBatchId);

    CityInfo findCityInfoByOrgId(@Param("orgId") int orgId);
    //根据注册的省份查询省级计划
    ProvinceBatch findByProvinceId(@Param("province") int provinceId);
}
