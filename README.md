直接使用 java -jar 启动，如果有中文乱码就手动指定一下UTF-8编码

数据库相关配置
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/bookstore
spring.datasource.username=mybatis_for_bookstore
spring.datasource.password=bookstore1116

数据库的表文件见“数据库设计.temp”

java版本
java version "21.0.9" 2025-10-21 LTS
Java(TM) SE Runtime Environment (build 21.0.9+7-LTS-338)
Java HotSpot(TM) 64-Bit Server VM (build 21.0.9+7-LTS-338, mixed mode, sharing)

maven 版本
Maven home: C:\Program Files\Java\apache-maven-3.9.11
Java version: 21.0.9, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-21.0.9
Default locale: zh_CN, platform encoding: UTF-8
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"

SpringBoot 版本 3.5.7


测试用账号
testuser_001
testuser_002
testuser_003    admin账号
testuser_004
testuser_005
testuser_006

所有密码为 StrongPassword123
JWT 过期10h

admin账号无法使用购物车和下单操作
user账号只能查看自己的订单

apifox
链接: https://s.apifox.cn/3a69aad8-cd9a-4b76-8b8f-bb1339c88d47  访问密码: U1vNz7Mc
