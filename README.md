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
