package edu.rutmiit.demo.auctionrest.event;

import edu.rutmiit.demo.auctioncontract.dto.LotResponse;
import edu.rutmiit.demo.events.AuctionEvent;
import edu.rutmiit.demo.events.EventEnvelope;
import edu.rutmiit.demo.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class LotEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(LotEventPublisher.class);
    private static final String SOURCE = "auction-rest";

    private final RabbitTemplate rabbitTemplate;

    public LotEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Публикует событие «лот создан».
     */
    public void publishCreated(LotResponse lot) {
        var event = new AuctionEvent.LotCreated(
                lot.getId(),
                lot.getTitle(),
                lot.getSeller().getId(),
                lot.getStartingPrice(),
                lot.getEndTime().toInstant()
        );
        send(RoutingKeys.LOT_CREATED, event);
    }

    /**
     * Публикует событие «торги по лоту завершены».
     */
    public void publishExpired(Long lotId, String title, Long winnerId, java.math.BigDecimal finalPrice, int totalBids) {
        var event = new AuctionEvent.LotExpired(lotId, title, winnerId, finalPrice, totalBids);
        send(RoutingKeys.LOT_EXPIRED, event);
    }

    /**
     * Публикует событие «лот продан».
     */
    public void publishSold(LotResponse lot) {
        var event = new AuctionEvent.LotSold(
                lot.getId(),
                lot.getTitle(),
                lot.getSeller().getId(),
                lot.getWinner() != null ? lot.getWinner().getId() : null,
                lot.getCurrentPrice()
        );
        send(RoutingKeys.LOT_SOLD, event);
    }

    private void send(String routingKey, AuctionEvent event) {
        try {
            EventEnvelope<AuctionEvent> envelope = EventEnvelope.wrap(event, SOURCE, routingKey);
            rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, routingKey, envelope);
            log.info("Событие отправлено: {} [eventId={}]", routingKey, envelope.metadata().eventId());
        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}", routingKey, e.getMessage());
        }
    }
}
