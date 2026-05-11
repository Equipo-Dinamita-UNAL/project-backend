FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
RUN apt-get update && apt-get install -y curl unzip
RUN curl https://start.spring.io/starter.zip \
    -d type=maven-project \
    -d language=java \
    -d bootVersion=3.3.0 \
    -d javaVersion=21 \
    -d artifactId=odontogate \
    -d groupId=com.odontogate \
    -d packaging=jar \
    -d dependencies=web,data-jpa,validation,lombok \
    -o demo.zip && \
    unzip demo.zip
RUN mvn install -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
