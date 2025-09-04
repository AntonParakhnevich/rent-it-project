@echo off
echo Запуск Rent-It Frontend...
echo.

cd frontend

echo Проверка Node.js...
node --version
if %errorlevel% neq 0 (
    echo ОШИБКА: Node.js не установлен. Пожалуйста, установите Node.js с https://nodejs.org/
    pause
    exit /b 1
)

echo.
echo Установка зависимостей...
npm install

echo.
echo Запуск приложения...
echo Приложение будет доступно по адресу: http://localhost:3000
echo Для остановки нажмите Ctrl+C
echo.

npm start

pause
