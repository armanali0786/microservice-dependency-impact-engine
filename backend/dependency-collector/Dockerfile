FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY backend/common/pom.xml backend/common/pom.xml
COPY backend/dependency-collector/pom.xml backend/dependency-collector/pom.xml
COPY backend/common/src backend/common/src
COPY backend/dependency-collector/src backend/dependency-collector/src
RUN mvn -pl backend/dependency-collector -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/backend/dependency-collector/target/dependency-collector-0.1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
