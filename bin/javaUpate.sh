#!/bin/bash

#JAVA VERSION
ROOT_PATH=D:/enterX
JAVA_PATH=$ROOT_PATH/java
NEW_JAVA_PATH=$ROOT_PATH/newClient/java
OLD_JAVA_PATH=$ROOT_PATH/bak/java
UPDATE_RESULT_FILE=$ROOT_PATH/bin/result
UPDATE_VERSION=$1
UPDATE_FILENAME=""

if [ ! -n "$1" ] ;then
    echo "you have not input a word!"
	exit 0
fi

UPDATE_FLG=FALSE
IFS=$(echo -en "\n\b")
if [ "`ls -A $NEW_JAVA_PATH`" = "" ]; then
  echo "java Client not need to update"
else
   if [ "UPDATE_VERSION" = "CURRENT_VERSION" ]; then 
	echo "tow version is same"
	java -jar D:/enterX/java/danmu_java_client.jar --spring.config.location=D:/enterX/java/application.properties
   else
        echo "you will update version is:"$UPDATE_VERSION
         #结束java
        echo "execute java update....."

        if [ ! -d "$OLD_JAVA_PATH" ]; then
          mkdir -p $OLD_JAVA_PATH
        else
            rm -rf $OLD_JAVA_PATH/*
        fi
        echo "move current to $OLD_JAVA_PATH"
        mv $JAVA_PATH/*.jar  $OLD_JAVA_PATH
        cp  $JAVA_PATH/version $OLD_JAVA_PATH
        cp  $JAVA_PATH/application.properties $OLD_JAVA_PATH


        cp -r $NEW_JAVA_PATH/"danmu_java_client_version.$UPDATE_VERSION.jar"  $JAVA_PATH/danmu_java_client.jar
        #echo -e $UPDATE_VERSION>$JAVA_PATH/version
	java -jar D:/enterX/java/danmu_java_client.jar --spring.config.location=D:/enterX/java/application.properties
   fi
fi
exit 0