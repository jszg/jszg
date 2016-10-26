package com.xtuer.mapper;

import com.xtuer.dto.Enrollhistory;
import com.xtuer.dto.Enrollment;
import com.xtuer.dto.HistoryValid;
import com.xtuer.dto.Limitation;
import com.xtuer.dto.OrgBatch;
import com.xtuer.dto.OrgBatchTime;
import com.xtuer.dto.Registration;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 共用Mappper
 *
 * Created by microacup on 2016/10/24.
 */
public interface CommonMapper {
    List<Limitation> findLimitation(@Param("idno") String idno, @Param("certno") String certno);
    List<Enrollhistory> findEnrollhistory(@Param("idno") String idno, @Param("certno") String certno);
    List<HistoryValid> findHistoryValid(@Param("idno") String idno, @Param("certno") String certno);
    HistoryValid findHistoryValidById(@Param("regId") int regId);
    List<Registration> findRegistration(@Param("idno") String idno, @Param("certno") String certno);
    Registration findRegistrationById(@Param("regId") int regId);
    List<Enrollment> findEnrollment(@Param("idno") String idno, @Param("certno") String certno);
    List<Enrollment> findEnrollmentStatus0(@Param("idno") String idno, @Param("certno") String certno);
    List<OrgBatch> findOrgBatch(@Param("orgId") int orgId);
    List<OrgBatchTime> findOrgBatchTime(@Param("orgId") int orgId);
}
