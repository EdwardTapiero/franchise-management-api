# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar solo pom.xml primero (para cachear dependencias)
COPY pom.xml .

# Descargar dependencias (esta capa se cachea)
RUN mvn dependency:go-offline -B

# Ahora copiar el código fuente
COPY src ./src

# Compilar (solo se ejecuta si cambió el código)
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre

WORKDIR /app

# Crear usuario no-root PRIMERO
RUN groupadd -r spring && useradd -r -g spring spring

# LUEGO crear directorio de logs con permisos
RUN mkdir -p /app/logs && chown -R spring:spring /app

# Copiar el JAR desde el stage de build
COPY --from=build /app/target/*.jar app.jar

# Cambiar permisos del JAR
RUN chown spring:spring app.jar

# Cambiar a usuario no-root
USER spring:spring

# Exponer puerto
EXPOSE 8080

# Variables de entorno por defecto
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV MONGODB_URI="mongodb://mongodb:27017/franchise_db"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]