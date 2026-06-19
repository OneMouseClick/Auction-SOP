package edu.rutmiit.demo.grpcverificationclient.publisher;

import edu.rutmiit.demo.events.AuctionEvent;
import edu.rutmiit.demo.events.EventEnvelope;
import edu.rutmiit.demo.events.RoutingKeys;
import edu.rutmiit.demo.grpc.VerifyUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class VerificationEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(VerificationEventPublisher.class);
    private static final String SOURCE = "grpc-verification-client";

    private final RabbitTemplate rabbitTemplate;

    public VerificationEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishVerified(Long userId, VerifyUserResponse response) {
        try {
            var event = new AuctionEvent.UserVerified(
                    userId,
                    response.getVerified(),
                    response.getRiskLevel().name(),
                    response.getLevel().name(),
                    response.getRecommendedAction(),
                    response.getConfidenceScore(),
                    response.getFlagsList(),
                    response.getMessage()
            );

            EventEnvelope<AuctionEvent> envelope = EventEnvelope.wrap(
                    event, SOURCE, RoutingKeys.USER_VERIFIED);

            rabbitTemplate.convertAndSend(
                    RoutingKeys.EXCHANGE,
                    RoutingKeys.USER_VERIFIED,
                    envelope);

            log.info("Событие отправлено: {} [userId={}, verified={}, eventId={}]",
                    RoutingKeys.USER_VERIFIED, userId, response.getVerified(),
                    envelope.metadata().eventId());

        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}",
                    RoutingKeys.USER_VERIFIED, e.getMessage());
        }
    }
}