package edu.rutmiit.demo.events;

import java.math.BigDecimal;

/**
 * Семейство событий аукциона.
 */
public sealed interface AuctionEvent {

    /**
     * Лот создан и ожидает проверки.
     */
    record LotCreated(
            Long lotId,
            String title,
            Long sellerId,
            BigDecimal startingPrice,
            java.time.Instant endTime
    ) implements AuctionEvent {}

    /**
     * Ставка размещена.
     */
    record BidPlaced(
            Long bidId,
            Long lotId,
            Long bidderId,
            BigDecimal amount,
            java.time.Instant placedAt
    ) implements AuctionEvent {}

    /**
     * Торги по лоту завершены (автоматически планировщиком).
     */
    record LotExpired(
            Long lotId,
            String title,
            Long winnerId,
            BigDecimal finalPrice,
            int totalBids
    ) implements AuctionEvent {}

    /**
     * Лот продан (финальное событие после истечения времени).
     */
    record LotSold(
            Long lotId,
            String title,
            Long sellerId,
            Long winnerId,
            BigDecimal finalPrice
    ) implements AuctionEvent {}
}
