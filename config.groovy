environments {
    development { // 本地开发环境，使用 MySQL
        staticBase = '/new-cert'
        uploadPersonImageDir = 'build'
        logDir = '/Users/Biao/Temp/logs'

        jdbc {
            driverClassName = 'com.mysql.jdbc.Driver'
            url = 'jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=UTF-8'
            username = 'root'
            password = 'root'
            validationQuery = 'SELECT 1'
        }

        redis {
            host = '127.0.0.1'
            port = 6379
        }
    }

    production { // 线上环境
        staticBase = 'http://static.jszg.edu.cn'
        uploadPersonImageDir = ''

        jdbc {
            driverClassName = 'oracle.jdbc.driver.OracleDriver'
            url = 'jdbc:oracle:thin:@192.168.10.86:1521:CERT'
            username = 'certification'
            password = '123456'
            validationQuery = 'SELECT 1 FROM dual'
        }

        redis {
            host = '127.0.0.1'
            port = 6379
        }
    }

    wxl { // 文显龙的开发环境，使用 Oracle
        staticBase = '/new-cert'
        uploadPersonImageDir = 'build'
        logDir = '/Users/Biao/Temp/logs'

        jdbc {
            driverClassName = 'oracle.jdbc.driver.OracleDriver'
            url = 'jdbc:oracle:thin:@192.168.10.25:1521:CERT'
            username = 'certification'
            password = '123456'
            validationQuery = 'SELECT 1 FROM dual'
        }

        redis {
            host = '127.0.0.1'
            port = 6379
        }
    }

    wxl2 { // 文显龙的开发环境，使用 Oracle
        staticBase = '/new-cert'
        uploadPersonImageDir = 'build'
        logDir = '/Users/Biao/Temp/logs'

        jdbc {
            driverClassName = 'oracle.jdbc.driver.OracleDriver'
            url = 'jdbc:oracle:thin:@192.168.10.25:1521:ORCL'
            username = 'wenxl'
            password = '123456'
            validationQuery = 'SELECT 1 FROM dual'
        }

        redis {
            host = '127.0.0.1'
            port = 6379
        }
    }

    jszgtest { // 北师大的测试环境，使用 Oracle
        staticBase = '/new-cert'
        uploadPersonImageDir = '/var/www/new_cert/photo/photo_cert'
        logDir = '/usr/local/tomcat8.5.6/logs'

        jdbc {
            driverClassName = 'oracle.jdbc.driver.OracleDriver'
            url = 'jdbc:oracle:thin:@172.16.4.2:1521:ORCL'
            username = 'certification'
            password = 'jszg649'
            validationQuery = 'SELECT 1 FROM dual'
        }

        redis {
            host = '192.168.8.116'
            port = 6379
        }
    }
}
