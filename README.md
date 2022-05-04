# Poshart 🎨
Poshart es una aplicación web destinada a la compraventa de arte ya sea pintura, dibujo, fotografía, escultura, impresiones, arte virtual y elementos relacionados. Los usuarios podrán dar _me gusta_ y comentar en las obras que deseen ayudando a decidir a otros usuarios acerca de la compra de esa obra en concreto.

## Vídeo 📺
[![Poshart | Funcionamiento de la web](https://img.youtube.com/vi/z-_W8c4hlaQ/0.jpg)](https://www.youtube.com/watch?v=z-_W8c4hlaQ "Poshart | Funcionamiento de la web")

## Funcionalidad pública 📢
* Observar las colecciones de obras de arte u obras en venta pertenecientes a usuarios privados.
* Observar los muros de otros usuarios.
* Busqueda de obras empleando los filtros deseados.
* Denunciar fraude o robo de obra.

## Funcionalidad privada 🔐
* Muro propio con las obras compradas en la aplicación.
* Muro propio de las obras de arte puestas a la venta.
* Seguir a otros usuarios.
* Darle _me gusta_ y comentar en las obras de otros usuarios.
* Crear colecciones de diferentes temáticas de obras _(lista de deseos, inspiración, futuras compras, etc)_.

## Entidades principales 📄
* **Obra**: Ítem puesto a la venta, comprado o en seguimiento.
* **Colección**: Alberga un conjunto de obras _(compradas o en venta)_ para un propósito específico.
* **Usuario**: Persona registrada en la aplicación, emplea tanto funcionalidades públicas como privadas.
* **Compra**: Obra comprada por un usuario a cambio de dinero.
* **Comentario**: Comentario de un usuario sobre una obra u otro comentario.
* **Imagen**: Entidad asociada tanto a las fotos de perfil como a las obras.

## Servicio interno 👮
* Confirmación de creación de cuenta.
* Envío de correo al usuario para confirmar la realización de una compra.
* Notificación cuando un usuario compra una obra en propiedad de otro usuario.
* Notificar al usuario cuando otro usuario ha comentado en alguna obra de su propiedad.

## Despliegue con docker compose 🐋
Para desplegar la aplicación teniendo docker compose (v.2.4.1) ya instalado solo necesitamos ejecutar el siguiente comando: ````curl -L https://bit.ly/3F6x6Hw | docker compose -f - up -d````

## Despliegue de la aplicación 📇
Para la compilación del proyecto vamos a seguir los siguientes pasos:
* Instalar el JDK y Maven.
* Añadir JDK y Maven a las variables de entorno.
* Abrimos una PowerShell en la carpeta del proyecto y ejecutamos ````mvn clean package````. Repetimos este paso por ambos proyectos.
* Para finalizar se nos habrá creado una carpeta Target con dentro el .jar que utilizaremos en la siguiente parte.

A continuación desplegamos la máquina virtual:
* Installar JDK con el comando ````sudo apt install openjdk-17-jre-headless````
* Installar y configurar MySQL con los comandos:
* ````sudo apt install mysql-server````
* ````sudo mysql````
* ````ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'GEIposhart#333';````
* ````flush privileges;````
* ````exit;````
* Iniciamos sesión en la base de datos ````sudo mysql -u root -p```` y utilizamos la contraseña que hemos usado previamente 'GEIposhart#333'.
* Creamos la base de datos ````CREATE DATABASE poshart;````
* ````USE poshart;````
* ````exit;````
* Por último ejecutamos el comando para descomprimir los .jar anteriores que hemos descargado en la máquina ````java -jar "nombreProyecto.jar"````

## Trello 🛠️
Para la planificación del proyecto se ha hecho uso de la herramienta virtual Trello.
* [Acceso al Trello](https://trello.com/b/WhTBchG9/ad)

## Modelo de Datos 📇
### Páginas principales
![Descripción Pantallas principales](https://user-images.githubusercontent.com/78254966/155119736-d1933ba6-1f0f-4a85-b0d8-47618bc5d369.png)
### Diagrama de navegación
![Diagrama de flujo](https://user-images.githubusercontent.com/78254966/155119901-9b898496-dd74-40bb-a3c7-cc925d60b805.jpg)
### Diagrama UML
![Diagrama UML (1)](https://user-images.githubusercontent.com/78254966/154862912-00f4e28c-75c6-4a15-8e59-8efe05a23014.png)
### Diagrama Entidad/Relación
![Diagrama E_R (2)](https://user-images.githubusercontent.com/78254966/155119384-fddbb7a7-896f-4313-80a2-48f10f26a037.png)
### Diagrama de Clases y Templates
![DiagramaClasesFase3](https://user-images.githubusercontent.com/78254966/162157514-fd664f1d-21e6-49e9-a88c-3b2528db102e.png)
### Diagrama de Infraestructura y Componentes
![Dibujo sin título](https://user-images.githubusercontent.com/78254966/166808732-0d790de4-0e13-4ead-acb4-625b4934d020.jpg)

## Autores ✒️️
* **Guillermo Juan García-Delgado Álvarez** - [Github](https://github.com/guilleingvid)
* **Eva Pastor Abánades** - [Github](https://github.com/evapastorabanades)
* **Iván Rodríguez García** - [Github](https://github.com/ivansploy)
