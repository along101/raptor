#!/bin/sh

APP_HOME=.

APP_MAINCLASS=org.openjdk.jmh.Main

CLASSPATH=$APP_HOME

for i in "$APP_HOME"/lib/*.jar; do
    CLASSPATH="$CLASSPATH":"$i"
done

JAVA_CMD="java -classpath $CLASSPATH $APP_MAINCLASS"

echo $JAVA_CMD

$JAVA_CMD