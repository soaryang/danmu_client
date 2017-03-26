#!/bin/bash

#JAVA VERSION
FLASH_PATH=D:/enterX/flash
NEW_FLASH_PATH=D:/enterX/newClient/flash
OLD_FLASH_PATH=D:/enterX/bak/flash
UPDATE_VERSION=$1
UPDATE_FILENAME=""
UPDATE_FLG=FALSE

if [ ! -n "$1" ] ;then
    echo "you have not input a word!"
	exit 0
fi


IFS=$(echo -en "\n\b")
if [ "`ls -A $NEW_FLASH_PATH`" = "" ]; then
  echo "flash Client not need to update"
else
    echo "you will execute flash update"
    if [ ! -d "$OLD_FLASH_PATH" ]; then
          mkdir -p $OLD_FLASH_PATH
    else
        rm -rf $OLD_FLASH_PATH/*
    fi

    for file in $FLASH_PATH/*
    do
        fileNameNoSuffix=$(basename $file)
        if [ "$fileNameNoSuffix" = "resource" ]; then
            continue;
        fi
		if [ "$fileNameNoSuffix" = "version" ]; then
	    mv $file  $OLD_FLASH_PATH
            continue;
        fi
        mv $file  $OLD_FLASH_PATH
    done
    D:
    cd  $NEW_FLASH_PATH
    tar xvf "flash_version.$UPDATE_VERSION.tar"  -C $FLASH_PATH
    
    #echo $VERSION_NUM>$FLASH_PATH/version
    java -jar D:/enterX/java/danmu_java_client.jar --spring.config.location=D:\enterX\java\application.properties 
fi
exit 0

