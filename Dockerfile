# Utiliza una imagen base de OpenJDK con JDK 17
FROM openjdk:17.0.2

# Establece un directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo .jar generado por Maven al contenedor
COPY ./target/cuentaMovimientos-0.0.1-SNAPSHOT.jar /app/cuentaMovimientos.jar

# Expone el puerto en el que corre la aplicación
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/cuentaMovimientos.jar"]
