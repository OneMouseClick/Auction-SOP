package edu.rutmiit.demo.auctionrest.event;

import edu.rutmiit.demo.auctioncontract.dto.UserResponse;
import edu.rutmiit.demo.events.AuctionEvent;
import edu.rutmiit.demo.events.EventEnvelope;
import edu.rutmiit.demo.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(UserEventPublisher.class);
    private static final String SOURCE = "auction-rest";

    private final RabbitTemplate rabbitTemplate;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUserRegistered(UserResponse user, String ipAddress, String userAgent) {
        var event = new AuctionEvent.UserRegistered(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null, // phone - если есть в UserResponse
                ipAddress != null ? ipAddress : "127.0.0.1",
                userAgent != null ? userAgent : "unknown",
                Instant.now()
        );

        try {
            EventEnvelope<AuctionEvent> envelope = EventEnvelope.wrap(event, SOURCE, RoutingKeys.USER_REGISTERED);
            rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, RoutingKeys.USER_REGISTERED, envelope);
            log.info("Событие отправлено: user.registered [userId={}]", user.getId());
        } catch (Exception e) {
            log.error("Не удалось отправить событие user.registered: {}", e.getMessage());
        }
    }
}