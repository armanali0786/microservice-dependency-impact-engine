FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY backend/common/pom.xml backend/common/pom.xml
COPY backend/dependency-graph-service/pom.xml backend/dependency-graph-service/pom.xml
COPY backend/common/src backend/common/src
COPY backend/dependency-graph-service/src backend/dependency-graph-service/src
RUN mvn -pl backend/dependency-graph-service -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/backend/dependency-graph-service/target/dependency-graph-service-0.1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
