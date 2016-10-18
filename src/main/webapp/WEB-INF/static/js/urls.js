Urls = {
    REST_CERT_TYPE: '/rest/signUp/certTypes',
    REST_PROVINCES: '/rest/signUp/provinces',
    REST_CITIES_BY_PROVINCE: '/rest/signUp/provinces/{provinceId}/cities',
    REST_SUBJECTS_ROOT: '/rest/signUp/provinces/{provinceId}/certTypes/{certTypeId}/subjects/root',
    REST_ORGS_BY_CITY_AND_CERT_TYPE: '/rest/signUp/cities/{cityId}/certTypes/{certTypeId}/orgs',
    REST_SUBJECTS_CHILDREN: '/rest/signUp/provinces/{provinceId}/{parentId}/subjects/children'
}
