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

}
