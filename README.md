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

#mvn clean package
#docker build
#docker build -t cuenta-moviemiento:1.0 .
#docker-compose up
#docker images
