@echo off
setlocal
set "PROJECT_DIR=%~dp0"
cd /d "%PROJECT_DIR%"
set "MAVEN_OPTS=-Duser.home=%PROJECT_DIR% -Dmaven.repo.local=%PROJECT_DIR%\.m2\repository"
call "%PROJECT_DIR%tools\maven\apache-maven-3.9.9\bin\mvn.cmd" spring-boot:run
