FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Instalar unzip limpiamente
RUN apt-get update && apt-get install -y --no-install-recommends unzip \
    && rm -rf /var/lib/apt/lists/*

COPY demo.zip .

# Descomprimir y compilar apuntando al pom.xml correcto
RUN unzip demo.zip
RUN mvn -f /app/demo/pom.xml install -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar desde la ruta correcta donde Maven genera el JAR
COPY --from=build /app/demo/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
