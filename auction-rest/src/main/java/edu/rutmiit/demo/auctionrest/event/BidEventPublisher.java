package edu.rutmiit.demo.auctionrest.event;

import edu.rutmiit.demo.auctioncontract.dto.BidResponse;
import edu.rutmiit.demo.events.AuctionEvent;
import edu.rutmiit.demo.events.EventEnvelope;
import edu.rutmiit.demo.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class BidEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(BidEventPublisher.class);
    private static final String SOURCE = "auction-rest";

    private final RabbitTemplate rabbitTemplate;

    public BidEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Публикует событие «ставка размещена».
     */
    public void publishBidPlaced(BidResponse bid) {
        var event = new AuctionEvent.BidPlaced(
                bid.getId(),
                bid.getLotId(),
                bid.getBidder().getId(),
                bid.getAmount(),
                bid.getPlacedAt().toInstant()
        );
        send(RoutingKeys.BID_PLACED, event);
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
