FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
RUN apt-get update && apt-get install -y curl unzip
RUN curl -L "https://start.spring.io/starter.zip\
?type=maven-project\
&language=java\
&bootVersion=3.3.0\
&javaVersion=21\
&artifactId=odontogate\
&groupId=com.odontogate\
&packaging=jar\
&dependencies=web,data-jpa,validation,lombok" \
    -o demo.zip && unzip demo.zip

RUN mvn install -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
