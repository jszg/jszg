# JSZG RestAPI



## 运行

* A. Oracle数据库

```
gradle -Denv=wxl clean appStartDebug
```

* B. 本地Mysql数据库

```
gradle -Denv=development clean appStartDebug
```

然后访问： [http://localhost:8080/](http://localhost:8080/)

## Rest API

* 所有资格种类：

    ```
    http://{{host}}:{{port}}/rest/signUp/certTypes
    ```

* 所有省份：

    ```
    http://{{host}}:{{port}}/rest/signUp/provinces
    ```

* 按省份找城市

    ```
    http://{{host}}:{{port}}/rest/signUp/provinces/{provinceid}/cities
    ```

* 认定机构

    ```
    http://{{host}}:{{port}}/rest/signUp/cities/{cityId}/certTypes/{certTypeId}/orgs
    ```
    
* 任教学科

    ```
    # 根节点
    http://{{host}}:{{port}}/rest/signUp/provinces/{provinceId}/certTypes/{certTypeId}/subjects/root
    
    # 子节点
    http://{{host}}:{{port}}/rest/signUp/provinces/{provinceId}/{parentId}/subjects/children
    ```
    
* 字典

    ```
    #所有
    http://{{host}}:{{port}}/rest/signUp/dicts
    
    #按类型    
    http://{{host}}:{{port}}/rest/signUp/dicts/{dictTypeId}
    ```
    