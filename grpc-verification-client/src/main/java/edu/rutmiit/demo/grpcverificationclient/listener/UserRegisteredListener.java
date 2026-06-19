package edu.rutmiit.demo.grpcverificationclient.listener;

import edu.rutmiit.demo.events.AuctionEvent;
import edu.rutmiit.demo.events.EventMetadata;
import edu.rutmiit.demo.grpc.*;
import edu.rutmiit.demo.grpcverificationclient.publisher.VerificationEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Component
public class UserRegisteredListener {

    private static final Logger log = LoggerFactory.getLogger(UserRegisteredListener.class);

    private final UserVerificationGrpc.UserVerificationBlockingStub verificationStub;
    private final VerificationEventPublisher eventPublisher;
    private final JsonMapper jsonMapper;

    public UserRegisteredListener(UserVerificationGrpc.UserVerificationBlockingStub verificationStub,
                                  VerificationEventPublisher eventPublisher,
                                  JsonMapper jsonMapper) {
        this.verificationStub = verificationStub;
        this.eventPublisher = eventPublisher;
        this.jsonMapper = jsonMapper;
    }

    @RabbitListener(queues = "q.verification.user-registered", messageConverter = "")
    public void handleUserRegistered(Message message) {
        try {
            // 1. Парсим JSON-конверт
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            JsonNode payloadNode = root.get("payload");
            AuctionEvent.UserRegistered userRegistered = jsonMapper.treeToValue(
                    payloadNode, AuctionEvent.UserRegistered.class
            );

            log.info("Получено событие user.registered: userId={}, username={}, email={} [eventId={}]",
                    userRegistered.userId(), userRegistered.username(),
                    userRegistered.email(), metadata.eventId());

            // 2. Формируем gRPC-запрос
            VerifyUserRequest grpcRequest = VerifyUserRequest.newBuilder()
                    .setUserId(userRegistered.userId())
                    .setUsername(userRegistered.username() != null ? userRegistered.username() : "")
                    .setEmail(userRegistered.email() != null ? userRegistered.email() : "")
                    .setPhone(userRegistered.phone() != null ? userRegistered.phone() : "")
                    .setIpAddress(userRegistered.ipAddress() != null ? userRegistered.ipAddress() : "127.0.0.1")
                    .setUserAgent(userRegistered.userAgent() != null ? userRegistered.userAgent() : "")
                    .setRegistrationDate(userRegistered.registrationDate() != null ?
                            userRegistered.registrationDate().toString() : "")
                    .setDeviceFingerprint("") // В реальном проекте передаем fingerprint
                    .build();

            // 3. Вызываем gRPC-сервер
            log.info("Вызов gRPC: UserVerification.VerifyUser(userId={})", userRegistered.userId());
            VerifyUserResponse grpcResponse = verificationStub.verifyUser(grpcRequest);

            log.info("gRPC ответ получен: userId={}, verified={}, risk={}, flags={}",
                    grpcResponse.getUserId(),
                    grpcResponse.getVerified(),
                    grpcResponse.getRiskLevel(),
                    grpcResponse.getFlagsList());

            // 4. Публикуем событие user.verified
            eventPublisher.publishVerified(userRegistered.userId(), grpcResponse);

            log.info("Пользователь верифицирован: userId={}, verified={}",
                    userRegistered.userId(), grpcResponse.getVerified());

        } catch (io.grpc.StatusRuntimeException e) {
            log.error("gRPC ошибка при верификации пользователя: {} ({})",
                    e.getStatus().getDescription(), e.getStatus().getCode());
            throw new RuntimeException("gRPC-вызов завершился ошибкой", e);
        } catch (Exception e) {
            log.error("Ошибка обработки события user.registered: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие user.registered", e);
        }
    }
}