#!/bin/sh

mvn install:install-file -Dfile=./target/hy.microservice-1.1.9.war     -DpomFile=./src/main/webapp/META-INF/maven/cn.openapis/hy.microservice/pom.xml
mvn install:install-file -Dfile=./target/hy.microservice-1.1.9-api.jar -DpomFile=./src/main/webapp/META-INF/maven/cn.openapis/hy.microservice/pom-api.xml


mvn deploy:deploy-file   -Dfile=./target/hy.microservice-1.1.9.war     -DpomFile=./src/main/webapp/META-INF/maven/cn.openapis/hy.microservice/pom.xml     -DrepositoryId=thirdparty -Durl=http://HY-ZhengWei:8081/repository/thirdparty
mvn deploy:deploy-file   -Dfile=./target/hy.microservice-1.1.9-api.jar -DpomFile=./src/main/webapp/META-INF/maven/cn.openapis/hy.microservice/pom-api.xml -DrepositoryId=thirdparty -Durl=http://HY-ZhengWei:8081/repository/thirdparty
