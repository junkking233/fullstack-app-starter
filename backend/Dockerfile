# syntax=docker/dockerfile:1
# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build

COPY pom.xml .
COPY settings.xml /root/.m2/settings.xml

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn package -DskipTests -B -q

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-noble

LABEL maintainer="template-team"
LABEL description="Template Backend Service"

RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/*

RUN groupadd --system --gid 1001 app && \
    useradd --system --uid 1001 --gid 1001 --no-create-home app

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar
RUN chown -R app:app /app

USER app

EXPOSE 8888

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD curl -sf http://localhost:8888/api/users?page=1 || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
