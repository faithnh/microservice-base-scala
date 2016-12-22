FROM mysql:5.7

ADD my.cnf /etc

ADD tables /docker-entrypoint-initdb.d/tables

## Mysql Imageは以下のディレクトリにsql及びshを配備するとコンテナ起動時に実行されます
ADD initialize.sh /docker-entrypoint-initdb.d
RUN chmod +x /docker-entrypoint-initdb.d/initialize.sh