package edu.rutmiit.demo.auctionrest.listener;

import edu.rutmiit.demo.auctioncontract.dto.UserResponse;
import edu.rutmiit.demo.auctionrest.storage.InMemoryStorage;
import edu.rutmiit.demo.events.AuctionEvent;
import edu.rutmiit.demo.events.EventMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Component
public class UserVerificationListener {

    private static final Logger log = LoggerFactory.getLogger(UserVerificationListener.class);

    private final InMemoryStorage storage;
    private final JsonMapper jsonMapper;

    public UserVerificationListener(InMemoryStorage storage, JsonMapper jsonMapper) {
        this.storage = storage;
        this.jsonMapper = jsonMapper;
    }

    @RabbitListener(queues = "q.auction.user-verified")
    public void handleUserVerified(Message message) {
        try {
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            JsonNode payloadNode = root.get("payload");
            AuctionEvent.UserVerified event = jsonMapper.treeToValue(
                    payloadNode, AuctionEvent.UserVerified.class
            );

            log.info("Получен результат верификации: userId={}, verified={}, risk={}, flags={} [eventId={}]",
                    event.userId(), event.verified(), event.riskLevel(), event.flags(), metadata.eventId());

            // Обновляем пользователя в хранилище
            UserResponse existing = storage.users.get(event.userId());
            if (existing != null) {
                UserResponse updated = UserResponse.builder()
                        .id(existing.getId())
                        .username(existing.getUsername())
                        .email(existing.getEmail())
                        .rating(existing.getRating())
                        .verified(event.verified())
                        .riskLevel(event.riskLevel())
                        .verificationLevel(event.verificationLevel())
                        .recommendedAction(event.recommendedAction())
                        .confidenceScore(event.confidenceScore())
                        .verificationFlags(event.flags())
                        .build();

                storage.users.put(event.userId(), updated);

                log.info("Пользователь {} обновлен: verified={}, risk={}",
                        updated.getUsername(), updated.getVerified(), updated.getRiskLevel());
            } else {
                log.warn("Пользователь с id={} не найден в хранилище", event.userId());
            }

        } catch (Exception e) {
            log.error("Ошибка обработки user.verified: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие user.verified", e);
        }
    }
}