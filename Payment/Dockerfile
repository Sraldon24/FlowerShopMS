# Stage 1: Build
FROM gradle:8.4-jdk17 AS build
WORKDIR /app

# Copy source code
COPY --chown=gradle:gradle . .

# Build the Spring Boot application
RUN gradle bootJar --no-daemon

# Stage 2: Run
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy the built jar from the Gradle container
COPY --from=build /app/build/libs/app.jar app.jar

# Expose default port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
