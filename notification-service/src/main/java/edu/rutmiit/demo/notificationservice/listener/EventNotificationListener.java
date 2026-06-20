package edu.rutmiit.demo.notificationservice.listener;

import edu.rutmiit.demo.events.AuctionEvent;
import edu.rutmiit.demo.events.EventMetadata;
import edu.rutmiit.demo.notificationservice.websocket.NotificationWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(EventNotificationListener.class);

    private final NotificationWebSocketHandler webSocketHandler;
    private final JsonMapper jsonMapper;

    private final Set<String> processedEventIds = ConcurrentHashMap.newKeySet();

    public EventNotificationListener(NotificationWebSocketHandler webSocketHandler,
                                     JsonMapper jsonMapper) {
        this.webSocketHandler = webSocketHandler;
        this.jsonMapper = jsonMapper;
    }

    @RabbitListener(queues = "q.notifications.all", messageConverter = "")
    public void handleEvent(Message message) {
        try {
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            // Дедупликация
            if (!processedEventIds.add(metadata.eventId())) {
                log.warn("Дубликат уведомления пропущен: eventId={}", metadata.eventId());
                return;
            }

            JsonNode payloadNode = root.get("payload");
            String title = buildTitle(metadata.eventType());
            String description = buildDescription(metadata.eventType(), payloadNode);
            String icon = resolveIcon(metadata.eventType());
            String level = resolveLevel(metadata.eventType());

            String notificationJson = jsonMapper.writeValueAsString(
                    new NotificationPayload(
                            "NOTIFICATION",
                            metadata.eventId(),
                            metadata.eventType(),
                            title,
                            description,
                            icon,
                            level,
                            metadata.source(),
                            metadata.timestamp().toString(),
                            Instant.now().toString()
                    )
            );

            webSocketHandler.broadcast(notificationJson);

            log.info("[NOTIFY] {} | {} (клиентов: {})",
                    metadata.eventType(), description, webSocketHandler.getActiveConnectionCount());

        } catch (Exception e) {
            log.error("Ошибка обработки события для уведомлений: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие", e);
        }
    }

    // ─── Формирование заголовков для событий аукциона ──────────────────

    private String buildTitle(String eventType) {
        return switch (eventType) {
            case "lot.created"   -> "Новый лот";
            case "lot.sold"      -> "Лот продан!";
            case "lot.expired"   -> "Торги завершены";
            case "bid.placed"    -> "Новая ставка";
            case "user.registered" -> "Новый пользователь";
            case "user.verified"   -> "Пользователь верифицирован";
            default              -> "Событие: " + eventType;
        };
    }

    private String buildDescription(String eventType, JsonNode payload) {
        try {
            return switch (eventType) {
                case "lot.created" -> {
                    AuctionEvent.LotCreated e = jsonMapper.treeToValue(payload, AuctionEvent.LotCreated.class);
                    yield "Создан лот «%s» (старт: %.2f ₽)".formatted(e.title(), e.startingPrice());
                }
                case "lot.sold" -> {
                    AuctionEvent.LotSold e = jsonMapper.treeToValue(payload, AuctionEvent.LotSold.class);
                    yield "Лот «%s» продан за %.2f ₽".formatted(e.title(), e.finalPrice());
                }
                case "lot.expired" -> {
                    AuctionEvent.LotExpired e = jsonMapper.treeToValue(payload, AuctionEvent.LotExpired.class);
                    yield "Торги по лоту «%s» завершены (ставок: %d)".formatted(e.title(), e.totalBids());
                }
                case "bid.placed" -> {
                    AuctionEvent.BidPlaced e = jsonMapper.treeToValue(payload, AuctionEvent.BidPlaced.class);
                    yield "Ставка %.2f ₽ на лот #%d".formatted(e.amount(), e.lotId());
                }
                case "user.registered" -> {
                    AuctionEvent.UserRegistered e = jsonMapper.treeToValue(payload, AuctionEvent.UserRegistered.class);
                    yield "Зарегистрирован пользователь «%s»".formatted(e.username());
                }
                case "user.verified" -> {
                    AuctionEvent.UserVerified e = jsonMapper.treeToValue(payload, AuctionEvent.UserVerified.class);
                    yield "Пользователь %s: %s".formatted(
                            e.verified() ? "✅ верифицирован" : "❌ отклонен",
                            e.message()
                    );
                }
                default -> "Неизвестное событие: " + eventType;
            };
        } catch (Exception e) {
            return "Событие " + eventType + " (ошибка парсинга)";
        }
    }

    private String resolveIcon(String eventType) {
        return switch (eventType) {
            case "lot.created"   -> "hammer";
            case "lot.sold"      -> "trophy";
            case "lot.expired"   -> "clock";
            case "bid.placed"    -> "coin";
            case "user.registered" -> "user-plus";
            case "user.verified"   -> "user-check";
            default              -> "bell";
        };
    }

    private String resolveLevel(String eventType) {
        return switch (eventType) {
            case "lot.sold"      -> "success";
            case "lot.expired"   -> "warning";
            case "bid.placed"    -> "info";
            default              -> "success";
        };
    }

    /**
     * Payload уведомления для WebSocket.
     */
    record NotificationPayload(
            String type,
            String eventId,
            String eventType,
            String title,
            String description,
            String icon,
            String level,
            String source,
            String eventTimestamp,
            String receivedAt
    ) {}
}