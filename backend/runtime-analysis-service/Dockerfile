FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY backend/common/pom.xml backend/common/pom.xml
COPY backend/runtime-analysis-service/pom.xml backend/runtime-analysis-service/pom.xml
COPY backend/common/src backend/common/src
COPY backend/runtime-analysis-service/src backend/runtime-analysis-service/src
RUN mvn -pl backend/runtime-analysis-service -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/backend/runtime-analysis-service/target/runtime-analysis-service-0.1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
