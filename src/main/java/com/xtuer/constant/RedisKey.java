package com.xtuer.constant;

/**
 * 各种数据在 Redis 中的 key
 */
public interface RedisKey {
    String CERT_TYPES = "certTypes"; // certTypes
    String PROVINCES = "provinces"; // provinces
    String CITIES = "cities_%d"; // cities_{provinceId}

    String ORGS = "orgs_%d_%d"; // orgs_{cityId}_{certTypeId}
    String ORGS_BY_ORGTYPE = "orgs_ct_%d"; // orgs_ct_{orgType}
    String ORGS_BY_PARENT = "orgs_p_%d"; // orgs_p_{parentId}
    String ORGS_BY_CITY = "orgs_c_%d"; // orgs_c_ {cityId}

    String SUBJECTS_ROOT = "subjects_root_%d_%d"; // subjects_root_{provinceId}_{certTypeId}
    String SUBJECTS_CHILDREN = "subjects_children_%d_%d"; // subjects_children_{provinceId}_{parentId}
    String SUBJECTS_BY_CERTTYPE = "subjects_ct_%d"; // subjects_ct_{certTypeId}
    String SUBJECTS_BY_PARENT = "subjects_p_%d"; // subjects_p_{parentId}
    String SUBJECTS_TEASUBJECT = "subjects_t_%d_%d"; // subjects_t_{subjectType}_{provinceId}

    String DICTS = "dicts"; // dicts
    String DICTS_BY_TYPE = "dicts_%d"; // dicts_{dictTypeId}
    String TEAGRADES = "teaGrades"; // teaGrades

    String COLLEGES = "colleges"; // colleges
    String COLLEGES_BY_PROVINCE = "colleges_p_%d"; // colleges_p_{provinceId}

    String MAJORS_ZHUCE_ROOT = "majors_root";
    String MAJORS_ZHUCE_CHILREN = "majors_children_%d"; // majors_chilren_{parentId}
    String MAJORS_RENDING_ROOT = "majors_rending_root_%d_%d"; // majors_rending_root_{certTypeId}_{eduLevelId}
    String MAJORS_RENDING_CHILDREN = "majors_rending_children_%d_%d"; // majors_rending_children_{provinceId}_{parentId}

    String TECHNICALJOBS = "technical_jobs"; // technical_jobs
    String TECHNICAL_JOB_CHILDREN = "technical_jobs_children_%d"; // technical_jobs_children_{parentId}
}
