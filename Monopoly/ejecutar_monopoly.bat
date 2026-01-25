@echo off
echo Compilando Monopoly...
if not exist out mkdir out
javac -encoding UTF-8 -d out -sourcepath src src\monopoly\main\Main.java

if %ERRORLEVEL% NEQ 0 (
    echo Error de compilacion.
    pause
    exit /b
)

echo Iniciando Juego...
java -cp out monopoly.main.Main
pause
