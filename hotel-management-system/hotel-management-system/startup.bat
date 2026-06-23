@echo off
setlocal enabledelayedexpansion

:: Set Java options
set JAVA_OPTS=-Xmx1024m -Xms256m -XX:MaxMetaspaceSize=256m -XX:+UseG1GC -XX:+UseStringDeduplication

:: Check for Maven in common locations
if exist "C:\Program Files\Apache\maven\bin\mvn.cmd" (
    set MAVEN_CMD="C:\Program Files\Apache\maven\bin\mvn.cmd"
) else if exist "C:\Program Files (x86)\Apache\maven\bin\mvn.cmd" (
    set MAVEN_CMD="C:\Program Files (x86)\Apache\maven\bin\mvn.cmd"
) else (
    echo Maven not found in default locations. Please install Maven and add it to your PATH.
    echo You can download Maven from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo Using Maven at: %MAVEN_CMD%
%MAVEN_CMD% spring-boot:run -Dspring-boot.run.jvmArguments="%JAVA_OPTS%"

if errorlevel 1 (
    echo.
    echo Error running the application. Please check the Maven output above for details.
    pause
    exit /b %errorlevel%
)
