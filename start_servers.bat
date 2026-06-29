@echo off
chcp 65001 >nul

echo ================================================
echo          企业招聘后台管理系统 - 一键启动
echo ================================================
echo.

:: 停止已运行的端口
echo [1/5] 停止已运行的服务...
call :stop_port 8080
call :stop_port 5174
timeout /t 1 /nobreak >nul
echo.

:: 编译后端
echo [2/5] 编译后端项目...
cd /d "%~dp0"
call mvnw.cmd clean package -DskipTests
if %errorlevel% neq 0 (
    echo 编译失败！
    pause
    exit /b 1
)
echo 编译成功！
echo.

:: 启动后端
echo [3/5] 启动后端服务 (端口: 8080)...
start "后端服务" cmd /k "java -jar target/demo01-0.0.1-SNAPSHOT.jar"
timeout /t 8 /nobreak >nul
echo.

:: 启动前端
echo [4/5] 启动前端服务 (端口: 5174)...
cd /d "%~dp0front"
start "前端服务" cmd /k "npm run dev"
timeout /t 3 /nobreak >nul
echo.

echo [5/5] 服务启动完成！
echo ================================================
echo   后端地址: http://localhost:8080
echo   前端地址: http://localhost:5174
echo ================================================
echo.
echo 按任意键停止所有服务并退出...
pause >nul

echo.
echo 正在停止所有服务...
call :stop_port 8080
call :stop_port 5174
echo 所有服务已停止！
exit /b 0

:: 停止指定端口的函数
:stop_port
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":%~1" ^| findstr "LISTENING"') do (
    echo   停止端口 %~1, PID: %%a
    taskkill /PID %%a /F >nul 2>&1
)
exit /b 0