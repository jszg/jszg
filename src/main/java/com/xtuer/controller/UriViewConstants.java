package com.xtuer.controller;

public interface UriViewConstants {
    String URI_HELLO  = "/hello";
    String VIEW_HELLO = "hello.fm";

    String REST_CERT_TYPE = "/rest/signUp/certTypes";
    String REST_PROVINCES = "/rest/signUp/provinces";
    String REST_CITIES_BY_PARENT = "/rest/signUp/provinces/{parentid}/cities";
    String REST_ORGS_BY_CT_AND_CITY = "/rest/signUp/orgs/{city}/{certTypeId}";
    String REST_SUBJECTS_ROOT = "/rest/signUp/subjects/root/{province}/{certTypeId}";
    String REST_SUBJECTS_CHILDREN = "/rest/signUp/subjects/children/{parent}/{province}";
}
