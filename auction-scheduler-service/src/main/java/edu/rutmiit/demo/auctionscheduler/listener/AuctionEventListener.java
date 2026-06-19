package edu.rutmiit.demo.auctionscheduler.listener;

import edu.rutmiit.demo.auctionscheduler.storage.LotTracker;
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
public class AuctionEventListener {

    private static final Logger log = LoggerFactory.getLogger(AuctionEventListener.class);

    private final LotTracker lotTracker;
    private final JsonMapper jsonMapper;

    public AuctionEventListener(LotTracker lotTracker, JsonMapper jsonMapper) {
        this.lotTracker = lotTracker;
        this.jsonMapper = jsonMapper;
    }

    @RabbitListener(queues = "q.scheduler.events")
    public void handleEvent(Message message) {
        try {
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            JsonNode payloadNode = root.get("payload");
            String eventType = metadata.eventType();

            switch (eventType) {
                case "lot.created" -> {
                    AuctionEvent.LotCreated event = jsonMapper.treeToValue(payloadNode, AuctionEvent.LotCreated.class);
                    lotTracker.track(new LotTracker.TrackedLot(
                            event.lotId(), event.title(), event.sellerId(), event.endTime()
                    ));
                    log.info("[SCHEDULER] Отслеживаю лот #{} «{}» до {}", event.lotId(), event.title(), event.endTime());
                }
                case "lot.sold", "lot.expired" -> {
                    Long lotId = payloadNode.get("lotId").asLong();
                    lotTracker.remove(lotId);
                    log.info("[SCHEDULER] Лот #{} снят с отслеживания", lotId);
                }
                case "user.registered", "user.verified" -> {
                    // Просто логируем события пользователей (не влияют на планировщик)
                    log.debug("[SCHEDULER] Получено событие пользователя: {}", eventType);
                }
                default -> log.debug("[SCHEDULER] Пропускаю событие: {}", eventType);
            }

        } catch (Exception e) {
            log.error("Ошибка обработки события", e);
            throw new RuntimeException("Не удалось обработать событие", e);
        }
    }
}