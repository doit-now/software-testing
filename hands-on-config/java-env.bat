@ECHO OFF

TITLE Thiet lap linh hoat bien moi truong trong Java va Tomcat - giao.lang 2022 

ECHO Chay file nay tu dong lenh cmd voi quyen Adminstrator

REM Thay the JDK cu bang JDK moi, va nguoc lai
ECHO Thay the JDK cu bang JDK moi, va nguoc lai
SETX JAVA_HOME "%JAVA_HOME:jdk1.8.0_251=jdk-18%" /m

REM Thay the Tomcat cu bang Tomcat moi, va nguoc lai
ECHO Thay the Tomcat cu bang Tomcat moi, va nguoc lai
SETX CATALINA_HOME "%CATALINA_HOME:apache-tomcat-8.5.81=apache-tomcat-10.0.22%" /m

PAUSE