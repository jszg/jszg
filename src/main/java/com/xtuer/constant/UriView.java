package com.xtuer.constant;

public interface UriView {
    String URI_HELLO  = "/hello";
    String VIEW_HELLO = "hello.fm";

    String REST_CERT_TYPE                  = "/rest/signUp/certTypes";
    String REST_PROVINCES                  = "/rest/signUp/provinces";
    String REST_CITIES_BY_PROVINCE         = "/rest/signUp/provinces/{provinceId}/cities";
    String REST_ORGS_BY_CITY_AND_CERT_TYPE = "/rest/signUp/cities/{cityId}/certTypes/{certTypeId}/orgs";
    String REST_SUBJECTS_ROOT              = "/rest/signUp/provinces/{provinceId}/certTypes/{certTypeId}/subjects/root";
    String REST_SUBJECTS_CHILDREN          = "/rest/signUp/provinces/{provinceId}/{parentId}/subjects/children";
    String REST_DICTS                      = "/rest/signUp/dicts";
    String REST_DICTS_BY_DICTTYPE          = "/rest/signUp/dicts/{dictTypeId}";
    String REST_COLLEGES                   = "/rest/signUp/colleges";
    String REST_COLLEGES_BY_PROVINCE       = "/rest/signUp/provinces/{provinceId}/colleges";
    String REST_ZHUCE_MAJOR_PARENT         = "/rest/signUp/majors/root";
    String REST_ZHUCE_MAJOR_CHILDREN       = "/rest/signUp/{parentId}/majors/children";
    String REST_RENDING_MAJOR_PARENT       = "/rest/signUp/certTypes/{certTypeId}/{eduLevelId}/majors/root";
    String REST_RENDING_MAJOR_CHILDREN     = "/rest/signUp/provinces/{provinceId}/{parentId}/majors/children";
    String REST_TECHNICAL_JOB_ROOT         = "/rest/signUp/technicaljobs/root";
    String REST_TECHNICAL_JOB_CHILDREN     = "/rest/signUp/{parentId}/technicaljobs/children";
}
