# syntax=docker/dockerfile:1
# Shared image for every iShop service. Pick the module with the SERVICE build arg,
# e.g. SERVICE=auth-service. Builds through the root Gradle wrapper so Docker uses
# the same Gradle and Spring Boot versions as local development.

FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace
COPY . .
# gradlew may be checked out with Windows line endings / without the exec bit.
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
ARG SERVICE
# The cache mount keeps the Gradle distribution and dependencies between builds.
# sharing=locked: services build in parallel, and Gradle's user home cannot be
# shared by concurrent builds, so they take turns on the cache instead.
RUN --mount=type=cache,target=/root/.gradle,sharing=locked \
    ./gradlew --no-daemon :${SERVICE}:bootJar -x test \
 && find ${SERVICE}/build/libs -name '*.jar' ! -name '*-plain.jar' -exec cp {} /workspace/app.jar \;

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/app.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
