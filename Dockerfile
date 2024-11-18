# Etapa de compilación
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY . .
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el archivo JAR generado
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto para Render
EXPOSE 8080

# Configurar las opciones de la JVM para optimizar el rendimiento
ENTRYPOINT ["java", "-Xms500m", "-Xmx512m", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "app.jar"]
HEALTHCHECK --interval=30s --timeout=10s --start-period=10s CMD curl -f http://localhost:8080/actuator/health || exit 1

