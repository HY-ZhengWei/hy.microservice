call mvn install:install-file -Dfile=./target/hy.microservice-1.0.9.war     -DpomFile=./src/main/webapp/META-INF/maven/cn.openapis/hy.microservice/pom.xml
call mvn install:install-file -Dfile=./target/hy.microservice-1.0.9-api.jar -DpomFile=./src/main/webapp/META-INF/maven/cn.openapis/hy.microservice/pom-api.xml


call mvn deploy:deploy-file   -Dfile=./target/hy.microservice-1.0.9.war     -DpomFile=./src/main/webapp/META-INF/maven/cn.openapis/hy.microservice/pom.xml     -DrepositoryId=thirdparty -Durl=http://HY-ZhengWei:8081/repository/thirdparty
call mvn deploy:deploy-file   -Dfile=./target/hy.microservice-1.0.9-api.jar -DpomFile=./src/main/webapp/META-INF/maven/cn.openapis/hy.microservice/pom-api.xml -DrepositoryId=thirdparty -Durl=http://HY-ZhengWei:8081/repository/thirdparty

pause
