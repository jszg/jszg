environments {
    development { // 开发环境
        staticBase = ''

        jdbc {
            driverClassName = 'com.mysql.jdbc.Driver'
            url = 'jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=UTF-8'
            username = 'root'
            password = 'root'
        }

        redis {
            host = '127.0.0.1'
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
