package edu.rutmiit.demo.auctionscheduler.scheduler;

import edu.rutmiit.demo.auctionscheduler.storage.LotTracker;
import edu.rutmiit.demo.events.AuctionEvent;
import edu.rutmiit.demo.events.EventEnvelope;
import edu.rutmiit.demo.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class LotExpirationScheduler {

    private static final Logger log = LoggerFactory.getLogger(LotExpirationScheduler.class);
    private static final String SOURCE = "auction-scheduler";

    private final LotTracker lotTracker;
    private final RabbitTemplate rabbitTemplate;

    public LotExpirationScheduler(LotTracker lotTracker, RabbitTemplate rabbitTemplate) {
        this.lotTracker = lotTracker;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Проверяет каждые 30 секунд, не истекло ли время лотов.
     */
    @Scheduled(fixedRate = 30000)
    public void checkExpiredLots() {
        List<LotTracker.TrackedLot> expired = lotTracker.getExpiredLots();
        
        for (LotTracker.TrackedLot lot : expired) {
            log.info("[SCHEDULER] Время лота #{} «{}» истекло! Публикую lot.expired", lot.lotId(), lot.title());
            
            var event = new AuctionEvent.LotExpired(
                    lot.lotId(),
                    lot.title(),
                    null,  // winnerId — будет заполнено основным сервисом
                    null,  // finalPrice
                    0      // totalBids
            );
            
            send(RoutingKeys.LOT_EXPIRED, event);
            lotTracker.remove(lot.lotId());
        }
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
