#!/bin/sh

mvn install:install-file -Dfile=./target/hy.microservice-1.12.3.war     -DpomFile=./src/main/webapp/META-INF/maven/cn.openapis/hy.microservice/pom.xml
mvn install:install-file -Dfile=./target/hy.microservice-1.12.3-api.jar -DpomFile=./src/main/webapp/META-INF/maven/cn.openapis/hy.microservice/pom-api.xml -Dclassifier=api


mvn deploy:deploy-file   -Dfile=./target/hy.microservice-1.12.3.war     -DpomFile=./src/main/webapp/META-INF/maven/cn.openapis/hy.microservice/pom.xml     -Dclassifier=api -DrepositoryId=thirdparty -Durl=http://HY-ZhengWei:8081/repository/thirdparty
mvn deploy:deploy-file   -Dfile=./target/hy.microservice-1.12.3-api.jar -DpomFile=./src/main/webapp/META-INF/maven/cn.openapis/hy.microservice/pom-api.xml -Dclassifier=api -DrepositoryId=thirdparty -Durl=http://HY-ZhengWei:8081/repository/thirdparty
