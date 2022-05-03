# Imagen base para el contenedor de proxy de sql maestro/esclavo
FROM proxysql/proxysql:2.3.2
COPY proxysql.cnf /etc/proxysql.cnf