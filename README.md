# Microservicio Cuenta-Movimientos

Este microservicio se encarga de gestionar cuentas y sus movimientos asociados. Utiliza **Spring Boot**, **Java** y **MySQL**. Se comunica de manera asíncrona con otros microservicios, como `clientePersona`.

## Requisitos

- Java 17
- Maven 3.6 o superior
- Docker (opcional, para levantar la base de datos)
- MySQL 8 o superior

## Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/jerry-rodrigo/cuentaMovimientosDevsu.git
cd cuentaMovimientos

#mvn clean package -DskipTests
#docker build -t cuenta-movimiento:1.0 .
#docker images
#podman run -d --name db -e MYSQL_ROOT_PASSWORD=MPeru123@ -e MYSQL_DATABASE=cuenta_movimiento_db -p 3306:3306 mysql:8
#docker run -p 8081:8081 6cc34a8ecc70

#PARA VER LOS CONTEINER:
#podman ps
#PARA DETERNLO
#podman stock 8034122361ed

#MYSQL
#url: jdbc:mysql://host.docker.internal:3306/cliente_persona_db
#podman run -d --name db -e MYSQL_ROOT_PASSWORD=MPeru123@ -e MYSQL_DATABASE=cliente_persona_db -p 3306:3306 mysql:8

#docker build -t cuenta-movimiento:1.0 .
#docker-compose up
#docker images
