### 项目说明
### 接口浏览 http://本机IP:端口号/项目前缀/swagger-ui.html
> 接口浏览：http://localhost:8989/anlian_sys/swagger-ui.html




### jar包注册maven 在idea的Terminal中运行包名前需要加 libs/ 如果在项目文件夹下打开cmd运行此命令的话则不需要加 libs/

- yuntian-common-3.0.0.jar
> mvn install:install-file -Dfile=libs/yuntian-common-3.0.0.jar -DgroupId=yuntian.common -DartifactId=yuntian-common -Dversion=3.0.0 -Dpackaging=jar

- javax.mail.jar
> mvn install:install-file -Dfile=libs/javax.mail.jar -DgroupId=yuntian.mail -DartifactId=yuntian-mail -Dversion=0.0.1 -Dpackaging=jar