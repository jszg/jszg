environments {
    development { // 开发环境
        staticBase = ''

        jdbc {
//            driverClassName = 'com.mysql.jdbc.Driver'
//            url = 'jdbc:mysql://192.168.10.116:3306/test?useUnicode=true&amp;characterEncoding=UTF-8'
            driverClassName = 'oracle.jdbc.driver.OracleDriver'
            url = 'jdbc:oracle:thin:@192.168.10.86:1521:orcl'
            username = 'wenxl'
            password = '123456'
        }

        redis {
            host = '192.168.10.116'
            port = 6379
        }
    }

    production { // 线上环境
        staticBase = 'http://static.jszg.edu.cn'

        jdbc {
            driverClassName = 'com.mysql.jdbc.Driver'
            url = 'jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=UTF-8'
            username = 'root'
            password = 'xxxx'
        }

        redis {
            host = '127.0.0.1'
            port = 6379
        }
    }
}
