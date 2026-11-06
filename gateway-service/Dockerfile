FROM gradle:8.10-jdk21 AS build
WORKDIR /workspace
COPY gateway-service ./gateway-service
RUN gradle -p gateway-service bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/gateway-service/build/libs/gateway-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
