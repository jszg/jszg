package com.xtuer.constant;

/**
 * 各种数据在 Redis 中的 key
 */
public interface RedisKey {
    String CERT_TYPES = "certTypes"; // certTypes
    String CERTTYPE_BY_TEACHGRADE = "certType_tg_%d"; // certType_tg_{teachGrade}

    String PROVINCES = "provinces"; // provinces
    String CITIES = "cities_%d"; // cities_{provinceId}

    String ORGS_BY_ORGTYPE = "orgs_ct_%d"; // orgs_ct_{orgType}
    String ORGS_BY_PARENT = "orgs_p_%d"; // orgs_p_{parentId}

    // 认定机构
    String REST_ORGS_REQUEST_BY_CERT_TYPE_PROVINCE_CITY = "orgs_q_%d_%d_%d_%d"; //orgs_q_{certTypeId}_{adminLevel}_{provinceId}_{cityId}

    // 注册机构
    String ORGS_ZHUCE = "orgs_r_%d_%d" ; //orgs_r_{teachGrade}_{cityId}

    String SUBJECTS_ROOT = "subjects_root_%d_%d"; // subjects_root_{provinceId}_{certTypeId}
    String SUBJECTS_CHILDREN = "subjects_children_%d_%d"; // subjects_children_{provinceId}_{parentId}
    String SUBJECTS_BY_CERTTYPE_ORG = "subjects_ct_%d_%d"; // subjects_ct_{certTypeId}_{orgId}
    String SUBJECTS_BY_PARENT = "subjects_p_%d"; // subjects_p_{parentId}
    String SUBJECTS_BY_PARENT_ORG_STATUS = "subjects_p_%d_%d_%d"; // subjects_p_{parentId}_{orgId}_{status}
    String SUBJECTS_TEASUBJECT = "subjects_t_%d_%d"; // subjects_t_{teachGrade}_{provinceId}
    String REST_REQUEST_SUBJECTS = "subjects_request_root_%d_%d"; // subjects_root_{provinceId}_{teachGrade}非统考第三步加载任教学科父节点
    String REST_REQUEST_SUBJECTS_CHILDREN = "subjects_request_children_%d_%d"; // subjects_root_{provinceId}_{parentId}非统考第三步加载任教学科子节点


    String DICTS = "dicts"; // dicts
    String DICTS_BY_TYPE = "dicts_%d"; // dicts_{dictTypeId}

    String TEAGRADES = "teaGrades"; // teaGrades
    String EDULEVELS = "eduLevels_%d"; // eduLevels_{certTypeId}
    String IDTYPRS = "idTypes_%d"; // idTypes_{certTypeId}

    String LOCAL_SETS = "localsets_%d_%d"; // localsets_{orgId}_{type}

    String COLLEGES = "colleges"; // colleges
    String COLLEGES_BY_PROVINCE = "colleges_p_%d"; // colleges_p_{provinceId}
    String DEGREES_BY_CERT_TYPE_EDU_LEVEL = "degrees_certtype_edulevel_%d_%d"; // degrees_certtype_edulevel_{certTypeId}_{eduLevelId}
    String DEGREES_BY_STATUS_TYPE ="degrees_status_type_%d_%d"; //degreess_status_type_{status}_{degrees}

    String MAJORS_ENROLL_ROOT = "majors_enroll_root_%d";//majors_enroll_root_{provinceId}
    String MAJORS_REQUEST_ROOT = "majors_request_root_%d";  //majors_request_root_{provinceId}
    String MAJORS_REQUEST_ROOT_CERTTYPE_EDULEVEL = "majors_request_root_%d_%d";  //majors_root_{certTypeId}_{eduLevelId}

    String MAJORS_REQUEST_CHILREN = "majors_request_children_%d_%d";  //majors_root_{provinceId}_{parentId}
    String MAJORS_ENROLL_CHILREN = "majors_children_%d_%d"; // majors_chilren_{provinceId}_{parentId}

    String TECHNICALJOBS = "technical_jobs"; // technical_jobs
    String TECHNICAL_JOB_CHILDREN = "technical_jobs_children_%d"; // technical_jobs_children_{parentId}

    String ACL_KEY = "acl";
}
