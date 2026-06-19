package edu.rutmiit.demo.auctionrest.controllers;

import edu.rutmiit.demo.auctioncontract.dto.*;
import edu.rutmiit.demo.auctioncontract.endpoints.LotApi;
import edu.rutmiit.demo.auctionrest.assemblers.BidModelAssembler;
import edu.rutmiit.demo.auctionrest.assemblers.LotModelAssembler;
import edu.rutmiit.demo.auctionrest.service.LotService;
import edu.rutmiit.demo.auctionrest.service.BidService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LotController implements LotApi {

    private final LotService lotService;
    private final BidService bidService;
    private final LotModelAssembler lotModelAssembler;
    private final BidModelAssembler bidModelAssembler;
    private final PagedResourcesAssembler<LotResponse> pagedLotsAssembler;
    private final PagedResourcesAssembler<BidResponse> pagedBidsAssembler;

    public LotController(LotService lotService,
                         BidService bidService,
                         LotModelAssembler lotModelAssembler,
                         BidModelAssembler bidModelAssembler,
                         PagedResourcesAssembler<LotResponse> pagedLotsAssembler,
                         PagedResourcesAssembler<BidResponse> pagedBidsAssembler) {
        this.lotService = lotService;
        this.bidService = bidService;
        this.lotModelAssembler = lotModelAssembler;
        this.bidModelAssembler = bidModelAssembler;
        this.pagedLotsAssembler = pagedLotsAssembler;
        this.pagedBidsAssembler = pagedBidsAssembler;
    }

    @Override
    public PagedModel<EntityModel<LotResponse>> getAllLots(LotStatus status, Long sellerId, int page, int size) {
        PagedResponse<LotResponse> paged = lotService.findAllLots(status, sellerId, page, size);
        PageImpl<LotResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedLotsAssembler.toModel(springPage, lotModelAssembler);
    }

    @Override
    public EntityModel<LotResponse> getLotById(Long id) {
        return lotModelAssembler.toModel(lotService.findLotById(id));
    }

    @Override
    public ResponseEntity<EntityModel<LotResponse>> createLot(LotCreateRequest request) {
        LotResponse created = lotService.createLot(request);
        EntityModel<LotResponse> model = lotModelAssembler.toModel(created);
        return ResponseEntity.ok(model);
    }

    @Override
    public EntityModel<LotResponse> updateLot(Long id, LotUpdateRequest request) {
        return lotModelAssembler.toModel(lotService.updateLot(id, request));
    }

    @Override
    public EntityModel<LotResponse> patchLot(Long id, PatchLotRequest request) {
        return lotModelAssembler.toModel(lotService.patchLot(id, request));
    }

    @Override
    public void deleteLot(Long id) {
        lotService.deleteLot(id);
    }

    @Override
    public PagedModel<EntityModel<BidResponse>> getBidsForLot(Long id, int page, int size) {
        lotService.findLotById(id);
        PagedResponse<BidResponse> paged = bidService.findBidsForLot(id, page, size);
        PageImpl<BidResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedBidsAssembler.toModel(springPage, bidModelAssembler);
    }
}