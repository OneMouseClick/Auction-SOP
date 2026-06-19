package edu.rutmiit.demo.auctionrest.controllers;

import edu.rutmiit.demo.auctioncontract.dto.*;
import edu.rutmiit.demo.auctioncontract.endpoints.BidApi;
import edu.rutmiit.demo.auctionrest.assemblers.BidModelAssembler;
import edu.rutmiit.demo.auctionrest.service.BidService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BidController implements BidApi {

    private final BidService bidService;
    private final BidModelAssembler bidModelAssembler;

    public BidController(BidService bidService,
                         BidModelAssembler bidModelAssembler) {
        this.bidService = bidService;
        this.bidModelAssembler = bidModelAssembler;
    }

    @Override
    public EntityModel<BidResponse> getBidById(Long id) {
        return bidModelAssembler.toModel(bidService.findBidById(id));
    }

    @Override
    public ResponseEntity<EntityModel<BidResponse>> placeBid(Long lotId, BidPlaceRequest request) {
        BidResponse bid = bidService.placeBid(lotId, request);
        EntityModel<BidResponse> model = bidModelAssembler.toModel(bid);
        return ResponseEntity.ok(model);
    }
}