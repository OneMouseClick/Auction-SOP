package edu.rutmiit.demo.auctionrest.service;

import edu.rutmiit.demo.auctioncontract.dto.*;
import edu.rutmiit.demo.auctioncontract.exception.ResourceNotFoundException;
import edu.rutmiit.demo.auctionrest.event.LotEventPublisher;
import edu.rutmiit.demo.auctionrest.storage.InMemoryStorage;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class LotService {
    
    private final InMemoryStorage storage;
    private final LotEventPublisher eventPublisher;
    
    public LotService(InMemoryStorage storage, LotEventPublisher eventPublisher) {
        this.storage = storage;
        this.eventPublisher = eventPublisher;
    }
    
    public LotResponse findLotById(Long id) {
        return Optional.ofNullable(storage.lots.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Lot", id));
    }
    
    public PagedResponse<LotResponse> findAllLots(LotStatus status, Long sellerId, int page, int size) {
        var stream = storage.lots.values().stream()
                .sorted(Comparator.comparingLong(LotResponse::getId));
        
        if (status != null) {
            stream = stream.filter(l -> l.getStatus() == status);
        }
        if (sellerId != null) {
            stream = stream.filter(l -> l.getSeller() != null && l.getSeller().getId().equals(sellerId));
        }
        
        List<LotResponse> allLots = stream.toList();
        int totalElements = allLots.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<LotResponse> content = (from >= totalElements) ? List.of() : allLots.subList(from, to);
        
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }
    
    public LotResponse createLot(LotCreateRequest request) {
        UserResponse seller = Optional.ofNullable(storage.users.get(request.sellerId()))
                .orElseThrow(() -> new ResourceNotFoundException("User", request.sellerId()));
        
        long id = storage.lotSequence.incrementAndGet();
        int durationHours = request.durationHours() != null ? request.durationHours() : 168;
        
        LotResponse lot = LotResponse.builder()
                .id(id)
                .title(request.title())
                .description(request.description())
                .seller(seller)
                .startingPrice(request.startingPrice())
                .currentPrice(request.startingPrice())
                .bidStep(request.bidStep())
                .startTime(OffsetDateTime.now())
                .endTime(OffsetDateTime.now().plus(durationHours, ChronoUnit.HOURS))
                .status(LotStatus.PENDING_VERIFICATION)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        
        storage.lots.put(id, lot);
        
        // Публикуем событие
        eventPublisher.publishCreated(lot);
        
        return lot;
    }
    
    public LotResponse updateLot(Long id, LotUpdateRequest request) {
        LotResponse existing = findLotById(id);
        
        LotResponse updated = LotResponse.builder()
                .id(id)
                .title(request.title())
                .description(request.description())
                .seller(existing.getSeller())
                .startingPrice(request.startingPrice())
                .currentPrice(existing.getCurrentPrice())
                .bidStep(request.bidStep())
                .startTime(existing.getStartTime())
                .endTime(existing.getEndTime())
                .status(existing.getStatus())
                .winner(existing.getWinner())
                .createdAt(existing.getCreatedAt())
                .updatedAt(OffsetDateTime.now())
                .build();
        
        storage.lots.put(id, updated);
        return updated;
    }
    
    public LotResponse patchLot(Long id, PatchLotRequest request) {
        LotResponse existing = findLotById(id);
        
        LotResponse updated = LotResponse.builder()
                .id(id)
                .title(request.title() != null ? request.title() : existing.getTitle())
                .description(request.description() != null ? request.description() : existing.getDescription())
                .seller(existing.getSeller())
                .startingPrice(existing.getStartingPrice())
                .currentPrice(existing.getCurrentPrice())
                .bidStep(request.bidStep() != null ? request.bidStep() : existing.getBidStep())
                .startTime(existing.getStartTime())
                .endTime(existing.getEndTime())
                .status(existing.getStatus())
                .winner(existing.getWinner())
                .createdAt(existing.getCreatedAt())
                .updatedAt(OffsetDateTime.now())
                .build();
        
        storage.lots.put(id, updated);
        return updated;
    }
    
    public void deleteLot(Long id) {
        LotResponse lot = findLotById(id);
        storage.lots.remove(id);
    }
}
