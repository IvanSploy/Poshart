# Imagen base para el contenedor de sql master
FROM mysql:5.7
COPY my.cnf /etc/mysql/my.cnf
COPY init.sql /docker-entrypoint-initdb.d/init.sql