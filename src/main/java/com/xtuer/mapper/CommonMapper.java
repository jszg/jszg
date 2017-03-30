package com.xtuer.mapper;

import com.xtuer.bean.UserPortalLog;
import com.xtuer.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 共用Mappper
 *
 * Created by microacup on 2016/10/24.
 */
public interface CommonMapper {
    List<Limitation> findLimitation(@Param("idNo") String idNo, @Param("certNo") String certNo);
    List<EnrollHistory> findEnrollHistory(@Param("idno") String idno, @Param("certno") String certno);
    List<HistoryValid> findHistoryValid(@Param("idno") String idno, @Param("certno") String certno);
    HistoryValid findHistoryValidById(@Param("regId") long regId);
    List<Registration> findRegistration(@Param("idno") String idno, @Param("certno") String certno);
    Registration findRegistrationById(@Param("regId") long regId);
    List<Enrollment> findEnrollment(@Param("idno") String idno, @Param("certno") String certno);
    List<Enrollment> findEnrollmentStatus0(@Param("idno") String idno, @Param("certno") String certno);
    List<OrgBatch> findOrgBatch(@Param("orgId") int orgId, @Param("type") int type);
    List<OrgBatchTime> findOrgBatchTime(@Param("orgId") int orgId, @Param("type") int type);

    // 插入UserPortalLog
    void insertUserPortalLog(UserPortalLog log);
    List<OrgBatchTime> findOrgBatchTimeByOrgBatchId(@Param("orgBatchId") int orgBatchId);

    CityInfo findCityInfoByOrgId(@Param("orgId") int orgId);
    //根据注册的省份查询省级计划
    ProvinceBatch findByProvinceId(@Param("provinceId") int provinceId);
    CertBatch findByYear(@Param("year") int year);

    //非统考认定第三步验证
    List<OrgCertType> findOrgCertTypeByOrgId(@Param("orgId") int orgId);

    //非统考第六步根据名称和证件号码查询限制库
    List<Limitation> findLimitationByNameAndIdNo(@Param("name") String name, @Param("idNo") String idNo);
    List<HistoryValid> findByUserYear(@Param("name") String name, @Param("idNo") String idNo, @Param("year") int year);
    List<Score> findByUserCertType(@Param("name") String name, @Param("idNo") String idNo, @Param("certTypeId") int certTypeId, @Param("subjectId") int subjectId);
    List<HistoryValid> checkHistoryExists(@Param("name") String name, @Param("idNo") String idNo, @Param("certTypeId") int certTypeId, @Param("subjectId") int subjectId);

    //保存简历信息
    long insertResume(Resume resume);

    //统考报名根据姓名,证件号和统考合格名单查询
    List<Score> findByUserExamNo(@Param("name") String name, @Param("idNo") String idNo, @Param("scoreCertNo") String scoreCertNo);

    //统考报名成功修改合格名单中的状态
    void updateScoreStatus(@Param("certBatchId") int certBatchId,@Param("status") int status, @Param("scoreId") long scoreId);

    Score findByScoreId(@Param("scoreId") long scoreId);

    //查询机构更名日志
    List<OrgNameLog> findOrgNameLog();

    //查询机构更名日志
    List<OrgNameLog> findByOldParentId(Integer parentId);

    //查询机构更名日志
    List<OrgNameLog> findOrgByNewDiffOrg(Integer parentId);

    //查询机构更名日志
    List<OrgAnnulLog> findOrgAnnulLogByParentId(Integer parentId);

    //查詢最高学历和资格种类关联表
    List<CertTypeLink> findByCertTypeAndEduLevel(@Param("certTypeId") int certTypeId, @Param("eduLevelId") int eduLevelId);
}
