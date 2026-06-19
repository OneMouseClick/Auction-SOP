package edu.rutmiit.demo.auctionrest.assemblers;

import edu.rutmiit.demo.auctioncontract.dto.LotResponse;
import edu.rutmiit.demo.auctionrest.controllers.LotController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LotModelAssembler implements RepresentationModelAssembler<LotResponse, EntityModel<LotResponse>> {

    @Override
    public EntityModel<LotResponse> toModel(LotResponse lot) {
        EntityModel<LotResponse> model = EntityModel.of(lot,
                linkTo(methodOn(LotController.class).getLotById(lot.getId())).withSelfRel(),
                linkTo(methodOn(LotController.class).getAllLots(null, null, 0, 20)).withRel("collection")
        );

        // Добавляем ссылку на ставки этого лота
        model.add(linkTo(methodOn(LotController.class)
                .getBidsForLot(lot.getId(), 0, 20)).withRel("bids"));

        return model;
    }
}