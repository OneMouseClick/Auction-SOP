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
@Relation(collectionRelation = "lots", itemRelation = "lot")
@Schema(description = "Информация о лоте")
public class LotResponse extends RepresentationModel<LotResponse> {

    @Schema(description = "Уникальный идентификатор лота", example = "1")
    private final Long id;

    @Schema(description = "Название лота", example = "Винтажные часы Rolex")
    private final String title;

    @Schema(description = "Подробное описание лота")
    private final String description;

    @Schema(description = "Продавец")
    private final UserResponse seller;

    @Schema(description = "Начальная цена", example = "100.00")
    private final BigDecimal startingPrice;

    @Schema(description = "Текущая цена (последняя ставка)", example = "150.50")
    private final BigDecimal currentPrice;

    @Schema(description = "Минимальный шаг ставки", example = "10.00")
    private final BigDecimal bidStep;

    @Schema(description = "Время начала торгов", example = "2024-01-01T10:00:00Z")
    private final OffsetDateTime startTime;

    @Schema(description = "Время окончания торгов", example = "2024-01-08T10:00:00Z")
    private final OffsetDateTime endTime;

    @Schema(description = "Текущий статус лота")
    private final LotStatus status;

    @Schema(description = "Победитель торгов (если статус SOLD)")
    private final UserResponse winner;

    @Schema(description = "Момент создания записи о лоте")
    private final OffsetDateTime createdAt;

    @Schema(description = "Момент последнего обновления (например, после ставки)")
    private final OffsetDateTime updatedAt;
}