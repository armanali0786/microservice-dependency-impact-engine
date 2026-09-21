FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY backend/common/pom.xml backend/common/pom.xml
COPY backend/service-registry/pom.xml backend/service-registry/pom.xml
COPY backend/common/src backend/common/src
COPY backend/service-registry/src backend/service-registry/src
RUN mvn -pl backend/service-registry -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/backend/service-registry/target/service-registry-0.1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
