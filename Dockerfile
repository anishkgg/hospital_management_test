# Stage 1: Build compilation
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Runtime image execution
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/hospital-management-test-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
