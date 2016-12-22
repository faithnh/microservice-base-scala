set SCRIPT_DIR=%~dp0
set SBT_OPTS=-Dfile.encoding=UTF-8 -Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M
java %SBT_OPTS% -jar "%SCRIPT_DIR%\sbt-launch-0.13.12.jar" %*