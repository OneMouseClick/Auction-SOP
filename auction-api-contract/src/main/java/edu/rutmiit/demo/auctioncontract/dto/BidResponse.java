package edu.rutmiit.demo.auctioncontract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "bids", itemRelation = "bid")
@Schema(description = "Информация о ставке")
public class BidResponse extends RepresentationModel<BidResponse> {

    @Schema(description = "Уникальный идентификатор ставки", example = "500")
    private final Long id;

    @Schema(description = "ID лота, на который сделана ставка", example = "1")
    private final Long lotId;

    @Schema(description = "Покупатель, сделавший ставку")
    private final UserResponse bidder;

    @Schema(description = "Сумма ставки", example = "150.50")
    private final BigDecimal amount;

    @Schema(description = "Время размещения ставки")
    private final OffsetDateTime placedAt;
}