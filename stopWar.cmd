@echo off
setlocal

jcmd | findstr littleMock > mypid.txt
set /p mydata=<mypid.txt
FOR /F "tokens=1-1 delims== " %%I IN ("%mydata%") DO (REM	echo %%I
 		taskkill /F /PID %%I
)


if ERRORLEVEL 1 GOTO ERROR

goto END
:ERROR
echo littleMock task could not be found!

:END
del mypid.txt