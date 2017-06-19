SET dir_bin=%~dp0
SET dir_bin=%dir_bin:~0,-1%

REM 
REM 1) JAVA_HOME\bin must be in the path for this script to work.
REM
REM 2) Download and unzip the following zip file.  Unzip gawk.exe and place it in a folder in the PATH
REM    http://unxutils.sourceforge.net/UnxUtils.zip

gawk.exe -f %dir_bin%\gctime.awk

where /q myapplication
IF ERRORLEVEL 1 (
    ECHO Download this zip file and put contents into the path:  http://unxutils.sourceforge.net/UnxUtils.zip
    EXIT /B
) ELSE (
    gawk.exe -f %dir_bin%\gctime.awk
)

