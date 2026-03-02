# multi-stage Dockerfile for building and running the Spring Boot backend

# ----- build stage -----
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /workspace
# copy pom and sources
COPY pom.xml .
COPY src ./src

# build the jar
RUN mvn clean package -DskipTests

# ----- runtime stage -----
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# copy the jar produced by the builder stage
COPY --from=builder /workspace/target/SafeZone-0.0.1-SNAPSHOT.jar app.jar

# optional: expose port
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
