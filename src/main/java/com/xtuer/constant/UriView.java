package com.xtuer.constant;

public interface UriView {
    String URI_HELLO  = "/hello";
    String VIEW_HELLO = "hello.fm";

    String REST_CERT_TYPE                  = "/rest/signUp/certTypes";
    String REST_PROVINCES                  = "/rest/signUp/provinces";
    String REST_CITIES_BY_PROVINCE        = "/rest/signUp/provinces/{provinceId}/cities";

    String REST_ORGS_REQUEST_BY_CERT_TYPE_PROVINCE_CITY   = "/rest/signUp/certTypes/{certTypeId}/adminLevel/{adminLevel}/provinces/{provinceId}/cities/{cityId}/orgs";
    String REST_ORGS_BY_ORGTYPE            = "/rest/signUp/orgtypes/{orgType}/orgs";
    String REST_ORGS_BY_PARENT             = "/rest/signUp/{parentId}/orgs";
    String REST_ORGS_REG                   = "/rest/signUp/reg/orgs";

    String REST_SUBJECTS_ROOT              = "/rest/signUp/provinces/{provinceId}/certTypes/{certTypeId}/subjects/root";
    String REST_SUBJECTS_CHILDREN          = "/rest/signUp/provinces/{provinceId}/teachGrade/{teachGrade}/{parentId}/subjects/children";
    String REST_SUBJECTS_BY_CERTTYPE_ORG   = "/rest/signUp/certTypes/{certTypeId}/org/{orgId}/subjects";
    String REST_SUBJECTS_BY_PARENT         = "/rest/signUp/{parentId}/subjects";

    String REST_SUBJECTS_BY_PARENT_BY_CERT_TYPE_ORG         = "/rest/signUp/parent/{parentId}/org/{orgId}/subjects";  //注册报名证书上的任教学科加载子节点
    String REST_SUBJECTS_TEASUBJECT        = "/rest/signUp/provinces/{provinceId}/teachGrades/{teachGrade}/subjects";
    String REST_TEACH_SUBJECT_BY_NAME     ="/rest/signUp/{teachGradeId}/{provinceId}/{name}/teachSubject";
    String REST_REQUEST_SUBJECTS           ="/rest/signUp/request/provinceId/{provinceId}/teachGrade/{teachGrade}/subjects";//非统考第三步任教学科父节点
    String REST_REQUEST_SUBJECTS_CHILDREN    ="/rest/signUp/request/provinceId/{provinceId}/teachGrade/{teachGrade}/parentId/{parentId}/subjects";//非统考第三步任教学科子节点
    String REST_REQUEST_SUBJECT_BY_NAME      ="/rest/signUp/request/teachGrade/{teachGrade}/provinceId/{provinceId}/name/{name}/subjects";//非统考第三步任教学科按名称查询
    String REST_DICTS                      = "/rest/signUp/dicts";
    String REST_DICTS_BY_DICTTYPE          = "/rest/signUp/dicts/{dictTypeId}";
    String REST_NATIONS                    = "/rest/signUp/nations";
    String REST_TEAGRADES                  = "/rest/signUp/teaGrades";
    String REST_EDU_LEVELS                  = "/rest/signUp/certTypes/{certTypeId}/eduLevels";
    String REST_ID_TYPE_CERT_TYPE          = "/rest/signUp/certTypes/{certTypeId}/idTypes";
    String REST_LOCAL_SETS                 = "/rest/signUp/orgs/{orgId}/type/{type}/localSets";
    String REST_LOCAL_SETS_INFO            = "/rest/signUp/localSets/{localSetId}";

    String REST_COLLEGES                   = "/rest/signUp/colleges";
    String REST_COLLEGES_BY_PROVINCE       = "/rest/signUp/provinces/{provinceId}/colleges";

    String REST_ENROLL_MAJOR_PARENT         = "/rest/signUp/province/{provinceId}/majors/root";
    String REST_ENROLL_MAJOR_CHILDREN             = "/rest/signUp/province/{provinceId}/{parentId}/majors/children";
    String REST_MAJOR_SEARCH_BY_NAME           = "/rest/signUp/province/{provinceId}/name/{name}/majors";
    String REST_MAJOR_SEARCH_BY_NAME_REQUEST      = "/rest/signUp/province/{provinceId}/name/{name}/certType/{certTypeId}/eduLevel/{eduLevelId}/majors";

    String REST_REQUEST_MAJOR_PARENT      = "/rest/signUp/provinceId/{provinceId}/certType/{certTypeId}/eduLevel/{eduLevelId}/request/majors/root";//非统考第七步所学专业root
    String REST_REQUEST_MAJOR_CHILDREN    = "/rest/signUp/provinceId/{provinceId}/{parentId}/request/majors/children";//非统考第七步所学专业children
    String REST_DEGREE_BY_CERT_TYPE_AND_EDU_LEVEL = "/rest/signUp/certType/{certTypeId}/eduLevel/{eduLevelId}/degrees";//认定报名根据资格种类和最高学历选择最高学位

    String REST_TECHNICAL_JOB_ROOT         = "/rest/signUp/technicaljobs/root";
    String REST_TECHNICAL_JOB_CHILDREN     = "/rest/signUp/{parentId}/technicaljobs/children";
    String REST_TECHNICAL_JOB_BY_NAME      = "/rest/signUp/{name}/technicaljobs";
    String REST_COLLEGE_BY_NAME             ="/rest/signUp/{name}/colleges";

    String REST_LIMITATION                 = "/rest/signUp/limitation";
    String REST_ENROLLHISTORY              = "/rest/signUp/enrollhistory";
    String REST_HISTORYVALID               = "/rest/signUp/historyvalid";
    String REST_REGISTRATION               = "/rest/signUp/registration";
    String REST_ENROLLMENT                 = "/rest/signUp/enrollment";

    // 验证相关的接口
    String REST_ENROLL_STEP3               = "/rest/signUp/enroll/step3";
    String REST_ENROLL_STEP4               = "/rest/signUp/enroll/step4";
    String REST_ENROLL_STEP7               = "/rest/signUp/enroll/step7";

    // 非统考第六步验证
    String REST_REQUEST_STEP6              = "/rest/signUp/request/step6";

    //统考第三步验证
    String REST_EXAM_STEP3                 = "/rest/signUp/exam/step3";


    // 验证注册的注册机构
    String REST_ENROLL_ORG_VALIDATION      = "/rest/signUp/enroll/orgs/{orgId}/validation";

    // 验证非统考的认定机构
    String REST_REQUEST_ORG_VALIDATION      = "/rest/signUp/request/orgs/{orgId}/certType/{certTypeId}/validation";

    // 验证统考的认定机构
    String REST_EXAM_ORG_VALIDATION      = "/rest/signUp/exam/orgs/{orgId}/certType/{certTypeId}/validation";

    String URI_ENROLL_SUBMIT               = "/enroll/submit";
    String URI_ENROLL_REG_PHOTO            = "/enroll/reg-photo/{enrollId}";
    String URI_UPLOAD_ENROLL_IMAGE         = "/enroll/upload-enroll-image";

    String URI_REQUEST_SUBMIT              = "/request/submit";

    String URI_EXAM_SUBMIT                 = "/exam/submit";

    // 访问控制
    String URI_ACL                  = "/a-JSZG-c-649-l-Enroll"; // 访问控制的页面
    String URI_ACL_COUNT            = "/acl/count"; // 查看人数
    String URI_ACL_MAX_COUNT        = "/acl/maxCount/{maxCount}"; // 修改人数限制
    String URI_ACL_MAX_DURATION     = "/acl/maxDuration/{maxDuration}"; // 修改人数限制
    String URI_ACL_CAN_ACCESS       = "/acl/canAccess"; // 是否可访问
    String URI_ACL_RESET            = "/acl/reset"; // 重置访问人数，即清空访问列表
    String URI_ACL_CAN_ACCESS_JSONP = "/acl/canAccessJsonp"; // 是否可访问
}
