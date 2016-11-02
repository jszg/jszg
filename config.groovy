environments {
    development { // 本地开发环境，使用 MySQL
        staticBase = '/new-cert'
        logDir = '/Users/Biao/Temp/logs'

        uploadTemp = '/var/www/upload/tmp'
        uploadEnrollPhotoDir = '/var/www/upload/storage/app_data/repository/photo/enroll_photo'
        uploadRegPhotoDir = '/var/www/upload/storage/app_data/repository/photo/enroll_photo'

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

    wxl1linux { // 文显龙的开发环境，使用 Oracle
        staticBase = '/new-cert'
        logDir = '/Users/Biao/Temp/logs'

        uploadTemp = '/var/www/upload/tmp'
        uploadEnrollPhotoDir = '/var/www/upload/storage/app_data/repository/photo/enroll_photo'
        uploadRegPhotoDir = '/var/www/upload/storage/app_data/repository/photo/enroll_photo'

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

    wxl1win { // 文显龙的开发环境，使用 Oracle
        staticBase = '/new-cert'
        logDir = 'D:/upload/tmp'

        uploadTemp = 'D:/upload/tmp'
        uploadEnrollPhotoDir = 'D:/upload/tmp'
        uploadRegPhotoDir = 'D:/upload/tmp'

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

    wxl2linux { // 文显龙的开发环境，使用 Oracle
        staticBase = '/new-cert'
        logDir = '/Users/Biao/Temp/logs'

        uploadTemp = '/var/www/upload/tmp'
        uploadEnrollPhotoDir = '/var/www/upload/storage/app_data/repository/photo/enroll_photo'
        uploadRegPhotoDir = '/var/www/upload/storage/app_data/repository/photo/enroll_photo'

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

    wxl2win { // 文显龙的开发环境，使用 Oracle
        staticBase = '/new-cert'
        logDir = 'D:/upload/tmp'

        uploadTemp = 'D:/upload/tmp'
        uploadEnrollPhotoDir = 'D:/upload/tmp'
        uploadRegPhotoDir = 'D:/upload/tmp'

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

    jszgtestlinux { // 北师大的测试环境，Linux 环境，使用 Oracle
        staticBase = '/new-cert'
        logDir = '/usr/local/tomcat8.5.6/logs'

        uploadTemp = '/var/www/upload/tmp'
        uploadEnrollPhotoDir = '/opt/photo/enroll_photo'
        uploadRegPhotoDir = '/opt/photo/reg_photo'

        jdbc {
            driverClassName = 'oracle.jdbc.driver.OracleDriver'
            url = 'jdbc:oracle:thin:@172.16.4.2:1521:ORCL'
            username = 'certification'
            password = 'jszg649'
            validationQuery = 'SELECT 1 FROM dual'
        }

        redis {
            host = '192.168.8.112'
            port = 6379
        }
    }

    jszgtestwin { // 北师大的测试环境，Windows 环境，使用 Oracle
        staticBase = '/new-cert'
        logDir = 'D:/upload/tmp'

        uploadTemp = 'D:/upload/tmp'
        uploadEnrollPhotoDir = 'D:/upload/tmp'
        uploadRegPhotoDir = 'D:/upload/tmp'

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

    jszg { // 北师大的正式环境，使用 Oracle
        staticBase = '/new-cert'
        logDir = '/usr/local/tomcat8.5.6/logs'

        uploadTemp = '/var/www/upload/tmp'
        uploadEnrollPhotoDir = '/var/www/upload/storage/app_data/repository/photo/enroll_photo'
        uploadRegPhotoDir = '/var/www/upload/storage/app_data/repository/photo/reg_photo'

        jdbc {
            driverClassName = 'oracle.jdbc.driver.OracleDriver'
            url = 'jdbc:oracle:thin:@192.168.10.86:1521:CERT'
            username = 'certification'
            password = '123456'
            validationQuery = 'SELECT 1 FROM dual'
        }

        redis {
            host = '192.168.8.116'
            port = 6379
        }
    }
}
