package com.xtuer.constant;

public interface UriView {
    String URI_HELLO  = "/hello";
    String VIEW_HELLO = "hello.fm";

    String REST_CERT_TYPE                  = "/rest/signUp/certTypes";
    String REST_PROVINCES                  = "/rest/signUp/provinces";
    String REST_CITIES_BY_PROVINCE         = "/rest/signUp/provinces/{provinceId}/cities";

    String REST_ORGS_RENDING               = "/rest/signUp/provinces/{provinceId}/cities/{cityId}/certTypes/{certTypeId}/orgs";
    String REST_ORGS_BY_ORGTYPE            = "/rest/signUp/orgtypes/{orgType}/orgs";
    String REST_ORGS_BY_PARENT             = "/rest/signUp/{parentId}/orgs";
    String REST_ORGS_REG                   = "/rest/signUp/reg/orgs";

    String REST_SUBJECTS_ROOT              = "/rest/signUp/provinces/{provinceId}/certTypes/{certTypeId}/subjects/root";
    String REST_SUBJECTS_CHILDREN          = "/rest/signUp/provinces/{provinceId}/{parentId}/subjects/children";
    String REST_SUBJECTS_BY_CERT_TYPE      = "/rest/signUp/certTypes/{certTypeId}/subjects";
    String REST_SUBJECTS_BY_PARENT         = "/rest/signUp/{parentId}/subjects";
    String REST_SUBJECTS_TEASUBJECT        = "/rest/signUp/provinces/{provinceId}/teachGrades/{teachGrade}/subjects";
    String REST_TEACH_SUBJECT_BY_NAME     ="/rest/signUp/{teachGradeId}/{provinceId}/{name}/teachSubject";

    String REST_DICTS                      = "/rest/signUp/dicts";
    String REST_DICTS_BY_DICTTYPE          = "/rest/signUp/dicts/{dictTypeId}";
    String REST_NATIONS                    = "/rest/signUp/nations";
    String REST_TEAGRADES                  = "/rest/signUp/teaGrades";
    String REST_EDULEVELS                  = "/rest/signUp/certTypes/{certTypeId}/eduLevels";
    String REST_ACADEMICDEGREE             = "/rest/signUp/certTypes/{certTypeId}/eduLevels/{eduLevel}/degrees";
    String REST_LOCAL_SETS                 = "/rest/signUp/orgs/{orgId}/localSets";
    String REST_LOCAL_SETS_INFO            = "/rest/signUp/localSets/{localSetId}";

    String REST_COLLEGES                   = "/rest/signUp/colleges";
    String REST_COLLEGES_BY_PROVINCE       = "/rest/signUp/provinces/{provinceId}/colleges";

    String REST_ZHUCE_MAJOR_PARENT         = "/rest/signUp/majors/root";
    String REST_RENDING_MAJOR_PARENT       = "/rest/signUp/certTypes/{certTypeId}/{eduLevelId}/majors/root";
    String REST_MAJOR_CHILDREN             = "/rest/signUp/{parentId}/majors/children";
    String REST_MAJOR_SEARCH_BY_NAME      = "/rest/signUp/{name}/majors";

    String REST_TECHNICAL_JOB_ROOT         = "/rest/signUp/technicaljobs/root";
    String REST_TECHNICAL_JOB_CHILDREN     = "/rest/signUp/{parentId}/technicaljobs/children";
    String REST_TECHNICAL_JOB_BY_NAME      = "/rest/signUp/{name}/technicaljobs";

    String REST_LIMITATION                 = "/rest/signUp/limitation";
    String REST_ENROLLHISTORY              = "/rest/signUp/enrollhistory";
    String REST_HISTORYVALID               = "/rest/signUp/historyvalid";
    String REST_REGISTRATION               = "/rest/signUp/registration";
    String REST_ENROLLMENT                 = "/rest/signUp/enrollment";

    // 验证相关的接口
    String REST_ENROLL_STEP3               = "/rest/signUp/enroll/step3";
    String REST_ENROLL_STEP4               = "/rest/signUp/enroll/step4";
    String REST_ENROLL_STEP7               = "/rest/signUp/enroll/step7";

    // 验证注册的注册机构
    String REST_ENROLL_ORG_VALIDATION      = "/rest/signUp/enroll/orgs/{orgId}/validation";

    String URI_ENROLL_SUBMIT               = "/enroll/submit";
    String URI_ENROLL_REG_PHOTO            = "/enroll/reg-photo/{enrollId}";
    String URI_UPLOAD_ENROLL_IMAGE         = "/enroll/upload-enroll-image";

    // 访问控制
    String URI_ACL                  = "/a-JSZG-c-649-l-Enroll"; // 访问控制的页面
    String URI_ACL_COUNT            = "/acl/count"; // 查看人数
    String URI_ACL_MAX_COUNT        = "/acl/maxCount/{maxCount}"; // 修改人数限制
    String URI_ACL_MAX_DURATION     = "/acl/maxDuration/{maxDuration}"; // 修改人数限制
    String URI_ACL_CAN_ACCESS       = "/acl/canAccess"; // 是否可访问
    String URI_ACL_RESET            = "/acl/reset"; // 重置访问人数，即清空访问列表
    String URI_ACL_CAN_ACCESS_JSONP = "/acl/canAccessJsonp"; // 是否可访问
}
