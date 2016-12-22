#!/bin/bash
SBT_OPTS="-Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled"
java $SBT_OPTS -jar `dirname $0`/sbt-launch-0.13.12.jar "$@"; exit $?