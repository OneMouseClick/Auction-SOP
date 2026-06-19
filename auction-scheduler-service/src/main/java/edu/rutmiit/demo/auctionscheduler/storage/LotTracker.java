package edu.rutmiit.demo.auctionscheduler.storage;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Отслеживает активные лоты и время их окончания.
 */
@Component
public class LotTracker {

    private final Map<Long, TrackedLot> activeLots = new ConcurrentHashMap<>();

    /**
     * Добавляет лот на отслеживание.
     */
    public void track(TrackedLot lot) {
        activeLots.put(lot.lotId(), lot);
    }

    /**
     * Возвращает все лоты, время которых истекло.
     */
    public java.util.List<TrackedLot> getExpiredLots() {
        Instant now = Instant.now();
        return activeLots.values().stream()
                .filter(l -> l.endTime().isBefore(now))
                .toList();
    }

    /**
     * Удаляет лот из отслеживания.
     */
    public void remove(Long lotId) {
        activeLots.remove(lotId);
    }

    public int activeCount() {
        return activeLots.size();
    }

    public java.util.Collection<TrackedLot> getAll() {
        return activeLots.values();
    }

    public record TrackedLot(
            Long lotId,
            String title,
            Long sellerId,
            Instant endTime
    ) {}
}
