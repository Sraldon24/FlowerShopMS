# Stage 1: Build
FROM gradle:8.4-jdk17 AS build
WORKDIR /app

COPY --chown=gradle:gradle . .

RUN gradle bootJar --no-daemon

# Stage 2: Run
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

COPY --from=build /app/build/libs/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
