@echo off
set SOURCES=src
set CLASSES=classes
setlocal enabledelayedexpansion
set CLASSPATH=
for %%i in (lib\*.jar) do (
    set CLASSPATH=!CLASSPATH!%%i;
)
javac -cp %CLASSPATH% -sourcepath %SOURCES% -d %CLASSES% %* src\*.java