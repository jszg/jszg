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

约定：

 > * [parameter=value] 代表该参数可选


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

* 认定的认定机构

    ```
    http://{{host}}:{{port}}/rest/signUp/provinces/{provinceid}/cities/{cityId}/certTypes/{certTypeId}/orgs
    ```

* 任教学科

    ```
    # 根节点
    http://{{host}}:{{port}}/rest/signUp/provinces/{provinceId}/certTypes/{certTypeId}/subjects/root

    # 子节点
    http://{{host}}:{{port}}/rest/signUp/provinces/{provinceId}/{parentId}/subjects/children
    ```

* 证书上的任教学科

    ```
    # 根节点
    http://{{host}}:{{port}}/rest/signUp/certTypes/{certTypeId}/subjects

    # 子节点
    http://{{host}}:{{port}}/rest/signUp/{parentId}/subjects
    ```

* 现任教学科

   ```
   # 父节点
   http://{{host}}:{{port}}/rest/signUp/provinces/{provinceId}/teachGrades/{teachGrade}/subjects

   # 子节点
   http://{{host}}:{{port}}/rest/signUp/provinces/{provinceId}/{parentId}/subjects/children
   ```

* 字典

    ```
    # 所有
    http://{{host}}:{{port}}/rest/signUp/dicts

    # 按类型
    http://{{host}}:{{port}}/rest/signUp/dicts/{dictTypeId}
    ```

* 毕业学校

    ```
    # 所有
    http://{{host}}:{{port}}/rest/signUp/colleges

    # 按省份找
    http://{{host}}:{{port}}/rest/signUp/provinces/{provinceId}/colleges
    ```

* 所学专业

    ```
    # 如果是认定的
    # 根节点
    http://{{host}}:{{port}}/rest/signUp/majors/root

    # 如果是注册的
    # 根节点
    http://{{host}}:{{port}}/rest/signUp/certTypes/{certTypeId}/{eduLevelId}/majors/root

    # 认定的或注册的子节点
    http://{{host}}:{{port}}/rest/signUp/{parentId}/majors/children
    ```

* 专业技术职务

    ```
    # 根节点
    http://{{host}}:{{port}}/rest/signUp/technicaljobs/root

    # 子节点
    http://{{host}}:{{port}}/rest/signUp/{parentId}/technicaljobs/children
    ```

* 民族

    ```
    http://{{host}}:{{port}}/rest/signUp/nations
    ```

* 证书上的机构

    ```
    # 根节点
    http://{{host}}:{{port}}/rest/signUp/orgtypes/{orgType}/orgs

    # 子节点
    http://{{host}}:{{port}}/rest/signUp/{parentId}/orgs
    ```


* 现任教学段

    ```
    http://{{host}}:{{port}}/rest/signUp/teaGrades
    ```

* 注册机构

    ```
    http://{{host}}:{{port}}/rest/signUp/reg/orgs?teachGrade={teachGrade}&cityId={cityId}[&provinceCity=true]
    ```

* 最高学历(认定)

    ```
    http://{{host}}:{{port}}/rest/signUp/certTypes/{certTypeId}/eduLevels
    ```

* 最高学位

    ```
    http://{{host}}:{{port}}/rest/signUp/certTypes/{certTypeId}/eduLevels/{eduLevel}/degrees
    ```

* 确认点

    ```
    http://{{host}}:{{port}}/rest/signUp/localsets?orgId={orgId}
    ```
    
* 限制库

    ```
    http://{{host}}:{{port}}/new-cert/rest/signUp/limitations?idno={idno}&certno={certno}
    ```