# Imagen base para el contenedor de haproxy para el servicio interno.
FROM haproxy:2.3
COPY haproxy.cfg /usr/local/etc/haproxy/haproxy.cfg
COPY errors/ /etc/haproxy/errors/
COPY errors/ /etc/haproxy/errors/
COPY ssl/ /etc/ssl/poshart/