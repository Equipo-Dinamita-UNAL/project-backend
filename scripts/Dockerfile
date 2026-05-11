FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Instalar unzip limpiamente
RUN apt-get update && apt-get install -y --no-install-recommends unzip \
    && rm -rf /var/lib/apt/lists/*

COPY demo.zip .

# Descomprimir y compilar — el ZIP extrae directo en /app sin subcarpeta
RUN unzip demo.zip
RUN mvn -f /app/pom.xml install -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

# El JAR queda en /app/target/ porque el pom.xml está en la raíz
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
