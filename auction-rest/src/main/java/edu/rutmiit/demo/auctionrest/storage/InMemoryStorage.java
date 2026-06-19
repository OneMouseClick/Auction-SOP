package edu.rutmiit.demo.auctionrest.storage;

import edu.rutmiit.demo.auctioncontract.dto.BidResponse;
import edu.rutmiit.demo.auctioncontract.dto.LotResponse;
import edu.rutmiit.demo.auctioncontract.dto.LotStatus;
import edu.rutmiit.demo.auctioncontract.dto.UserResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStorage {

    public final Map<Long, LotResponse> lots = new ConcurrentHashMap<>();
    public final Map<Long, BidResponse> bids = new ConcurrentHashMap<>();
    public final Map<Long, UserResponse> users = new ConcurrentHashMap<>();

    public final AtomicLong lotSequence = new AtomicLong(0);
    public final AtomicLong bidSequence = new AtomicLong(0);
    public final AtomicLong userSequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
        UserResponse seller = UserResponse.builder()
                .id(userSequence.incrementAndGet())
                .username("antique_master")
                .email("antique@example.com")
                .rating(98.5)
                .build();

        UserResponse buyer = UserResponse.builder()
                .id(userSequence.incrementAndGet())
                .username("collector_joe")
                .email("joe@example.com")
                .rating(95.0)
                .build();

        users.put(seller.getId(), seller);
        users.put(buyer.getId(), buyer);

        long lotId1 = lotSequence.incrementAndGet();
        LotResponse lot1 = LotResponse.builder()
                .id(lotId1)
                .title("Винтажные часы Rolex")
                .description("Редкие часы Rolex 1950 года в отличном состоянии")
                .seller(seller)
                .startingPrice(new BigDecimal("100.00"))
                .currentPrice(new BigDecimal("150.00"))
                .bidStep(new BigDecimal("10.00"))
                .startTime(OffsetDateTime.now().minus(1, ChronoUnit.HOURS))
                .endTime(OffsetDateTime.now().plus(7, ChronoUnit.DAYS))
                .status(LotStatus.ACTIVE)
                .createdAt(OffsetDateTime.now().minus(1, ChronoUnit.HOURS))
                .updatedAt(OffsetDateTime.now())
                .build();
        lots.put(lotId1, lot1);

        long bidId1 = bidSequence.incrementAndGet();
        BidResponse bid1 = BidResponse.builder()
                .id(bidId1)
                .lotId(lotId1)
                .bidder(buyer)
                .amount(new BigDecimal("150.00"))
                .placedAt(OffsetDateTime.now().minus(30, ChronoUnit.MINUTES))
                .build();
        bids.put(bidId1, bid1);
    }
}