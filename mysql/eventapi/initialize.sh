#!/bin/sh

echo "Create Table"

export MYSQL_PWD=${MYSQL_PASSWORD}

mysql -u ${MYSQL_USER} --database ${MYSQL_DATABASE} -e "source /docker-entrypoint-initdb.d/tables/tables.sql"
mysql -u ${MYSQL_USER} --database ${MYSQL_DATABASE} -e "source /docker-entrypoint-initdb.d/tables/data.sql"