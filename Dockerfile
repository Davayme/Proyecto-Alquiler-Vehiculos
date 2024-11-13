# Etapa 1: Build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Copiar solo los archivos necesarios para la compilación
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM openjdk:17-alpine
WORKDIR /app
# Copiar el JAR generado desde la etapa de build
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar
# Exponer el puerto 8080
EXPOSE 8080
# Configurar las opciones de la JVM para mejorar el rendimiento
ENTRYPOINT ["java", "-Xms128m", "-Xmx510m", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "-XX:+UseStringDeduplication", "-jar", "app.jar"]

