@echo off
echo 正在停止运行中的服务端口...

:: 停止8080端口（后端服务）
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8080" ^| findstr "LISTENING"') do (
    echo 正在停止端口8080，PID: %%a
    taskkill /PID %%a /F >nul 2>&1
)

:: 停止5173/5174端口（前端服务）
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":5173 :5174" ^| findstr "LISTENING"') do (
    echo 正在停止端口，PID: %%a
    taskkill /PID %%a /F >nul 2>&1
)

echo 所有端口已停止！
pause