package com.xtuer.constant;

public interface UriView {
    String URI_HELLO  = "/hello";
    String VIEW_HELLO = "hello.fm";

    String REST_CERT_TYPE                  = "/rest/signUp/certTypes";
    String REST_PROVINCES                  = "/rest/signUp/provinces";
    String REST_CITIES_BY_PARENT           = "/rest/signUp/provinces/{provinceId}/cities";
    String REST_ORGS_BY_CITY_AND_CERT_TYPE = "/rest/signUp/orgs/{city}/{certTypeId}";
    String REST_SUBJECTS_ROOT              = "/rest/signUp/subjects/root/{provinceId}/{certTypeId}";
    String REST_SUBJECTS_CHILDREN          = "/rest/signUp/subjects/children/{provinceId}/{parentId}";
}
