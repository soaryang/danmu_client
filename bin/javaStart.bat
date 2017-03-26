@echo off

cmd /c
d:
rsync -arvIz --delete --password-file=D:/enterX/rsync/rsync.secrets rsync_user@101.201.80.206::initrsync /enterX/initrsync
java -jar D:/enterX/java/danmu_java_client.jar --spring.config.location=D:\enterX\java\application.properties