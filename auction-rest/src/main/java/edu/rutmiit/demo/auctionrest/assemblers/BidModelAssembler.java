package edu.rutmiit.demo.auctionrest.assemblers;

import edu.rutmiit.demo.auctioncontract.dto.BidResponse;
import edu.rutmiit.demo.auctionrest.controllers.BidController;
import edu.rutmiit.demo.auctionrest.controllers.LotController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BidModelAssembler implements RepresentationModelAssembler<BidResponse, EntityModel<BidResponse>> {

    @Override
    public EntityModel<BidResponse> toModel(BidResponse bid) {
        return EntityModel.of(bid,
                linkTo(methodOn(BidController.class).getBidById(bid.getId())).withSelfRel(),
                linkTo(methodOn(LotController.class).getBidsForLot(bid.getLotId(), 0, 20)).withRel("lot-bids"),
                linkTo(methodOn(LotController.class).getLotById(bid.getLotId())).withRel("lot")
        );
    }
}