package edu.rutmiit.demo.auctionrest.service;

import edu.rutmiit.demo.auctioncontract.dto.*;
import edu.rutmiit.demo.auctioncontract.exception.BidTooLowException;
import edu.rutmiit.demo.auctioncontract.exception.LotNotActiveException;
import edu.rutmiit.demo.auctioncontract.exception.ResourceNotFoundException;
import edu.rutmiit.demo.auctionrest.event.BidEventPublisher;
import edu.rutmiit.demo.auctionrest.storage.InMemoryStorage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BidService {
    
    private final InMemoryStorage storage;
    private final BidEventPublisher eventPublisher;
    
    public BidService(InMemoryStorage storage, BidEventPublisher eventPublisher) {
        this.storage = storage;
        this.eventPublisher = eventPublisher;
    }
    
    public BidResponse findBidById(Long id) {
        return Optional.ofNullable(storage.bids.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Bid", id));
    }
    
    public PagedResponse<BidResponse> findBidsForLot(Long lotId, int page, int size) {
        List<BidResponse> allBids = storage.bids.values().stream()
                .filter(b -> b.getLotId().equals(lotId))
                .sorted(Comparator.comparingLong(BidResponse::getId))
                .toList();
        
        int totalElements = allBids.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<BidResponse> content = (from >= totalElements) ? List.of() : allBids.subList(from, to);
        
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }
    
    public BidResponse placeBid(Long lotId, BidPlaceRequest request) {
        LotResponse lot = Optional.ofNullable(storage.lots.get(lotId))
                .orElseThrow(() -> new ResourceNotFoundException("Lot", lotId));
        
        if (lot.getStatus() != LotStatus.ACTIVE) {
            throw new LotNotActiveException(lotId);
        }
        
        UserResponse bidder = Optional.ofNullable(storage.users.get(request.bidderId()))
                .orElseThrow(() -> new ResourceNotFoundException("User", request.bidderId()));
        
        BigDecimal minRequired = lot.getCurrentPrice().add(lot.getBidStep());
        if (request.amount().compareTo(minRequired) < 0) {
            throw new BidTooLowException(request.amount(), minRequired);
        }
        
        long bidId = storage.bidSequence.incrementAndGet();
        BidResponse bid = BidResponse.builder()
                .id(bidId)
                .lotId(lotId)
                .bidder(bidder)
                .amount(request.amount())
                .placedAt(OffsetDateTime.now())
                .build();
        
        storage.bids.put(bidId, bid);
        
        LotResponse updatedLot = LotResponse.builder()
                .id(lot.getId())
                .title(lot.getTitle())
                .description(lot.getDescription())
                .seller(lot.getSeller())
                .startingPrice(lot.getStartingPrice())
                .currentPrice(request.amount())
                .bidStep(lot.getBidStep())
                .startTime(lot.getStartTime())
                .endTime(lot.getEndTime())
                .status(lot.getStatus())
                .winner(bidder)
                .createdAt(lot.getCreatedAt())
                .updatedAt(OffsetDateTime.now())
                .build();
        
        storage.lots.put(lotId, updatedLot);
        
        // Публикуем событие о новой ставке
        eventPublisher.publishBidPlaced(bid);
        
        return bid;
    }
}
