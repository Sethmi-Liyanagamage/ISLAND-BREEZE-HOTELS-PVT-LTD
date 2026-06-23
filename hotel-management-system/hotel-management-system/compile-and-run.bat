@echo off
echo Compiling Hotel Management System...

REM Create target directory structure
if not exist "target\classes" mkdir target\classes
if not exist "target\lib" mkdir target\lib

REM Copy resources
if not exist "target\classes\static" mkdir target\classes\static
xcopy /E /Y "src\main\resources\static\*" "target\classes\static\"
copy /Y "src\main\resources\application.properties" "target\classes\"

echo.
echo Note: This is a simplified compilation script.
echo For full functionality, you would need Maven to download dependencies.
echo.
echo To run the application, you can:
echo 1. Use the existing JAR file: java -jar target/tutor-management-system-1.0.0.jar
echo 2. Or install Maven and run: mvn clean package
echo.
pause
