package edu.rutmiit.demo.auctioncontract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Запрос на размещение ставки")
public record BidPlaceRequest(

        @Schema(description = "ID покупателя", example = "456", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "ID покупателя не может быть пустым")
        Long bidderId,

        @Schema(description = "Сумма ставки", example = "160.00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Сумма ставки не может быть пустой")
        @DecimalMin(value = "0.01", message = "Сумма ставки должна быть больше 0")
        BigDecimal amount
) {}