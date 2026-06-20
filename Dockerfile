# Dockerfile - только упаковывает уже собранные JAR
# Сборка выполняется локально через mvnw

# Базовый образ
FROM eclipse-temurin:21-jre-alpine AS base
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# ─── Auction REST ──────────────────────────────────────────────────────────────
FROM base AS auction-rest
COPY auction-rest/target/auction-rest-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# ─── Auction Scheduler ─────────────────────────────────────────────────────────
FROM base AS auction-scheduler
COPY auction-scheduler-service/target/auction-scheduler-service-*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]

# ─── gRPC Verification Server ─────────────────────────────────────────────────
FROM base AS grpc-verification-server
COPY grpc-verification-service/target/grpc-verification-service-*.jar app.jar
EXPOSE 8083 9090
ENTRYPOINT ["java", "-jar", "app.jar"]

# ─── gRPC Verification Client ─────────────────────────────────────────────────
FROM base AS grpc-verification-client
COPY grpc-verification-client/target/grpc-verification-client-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

# ─── Notification Service ──────────────────────────────────────────────────────
FROM base AS notification-service
COPY notification-service/target/notification-service-*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]