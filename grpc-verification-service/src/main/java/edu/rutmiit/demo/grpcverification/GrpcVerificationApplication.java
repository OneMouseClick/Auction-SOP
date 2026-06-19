package edu.rutmiit.demo.grpcverification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * gRPC Verification Server — сервис верификации пользователей.
 *
 * Запускает Spring Boot приложение и gRPC-сервер на порту 9090.
 * HTTP-порт (8083) используется для actuator/health endpoints.
 *
 * Запуск: ./mvnw spring-boot:run -pl grpc-verification-service -am
 */
@SpringBootApplication
public class GrpcVerificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrpcVerificationApplication.class, args);
    }
}