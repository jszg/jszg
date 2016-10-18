package com.xtuer.constant;

/**
 * 各种数据在 Redis 中的 key
 */
public interface RedisKey {
    String CERT_TYPES = "certTypes"; // certTypes
    String PROVINCES = "provinces"; // provinces
    String CITIES = "cities_%d"; // cities_{provinceId}
    String ORGS = "orgs_%d_%d"; // orgs_{cityId}_{certTypeId}
    String SUBJECTS_ROOT = "subjects_root_%d_%d"; // subjects_root_{provinceId}_{certTypeId}
    String SUBJECTS_CHILDREN = "subjects_children_%d_%d"; // subjects_children_{provinceId}_{parentId}
    String DICTS = "dicts_%d"; // subjects_children_{provinceId}_{parentId}
}
