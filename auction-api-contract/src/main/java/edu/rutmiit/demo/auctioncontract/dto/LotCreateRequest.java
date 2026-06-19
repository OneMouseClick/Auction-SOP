package edu.rutmiit.demo.auctioncontract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Запрос на создание нового лота")
public record LotCreateRequest(

        @Schema(description = "ID продавца", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "ID продавца не может быть пустым")
        Long sellerId,

        @Schema(description = "Название лота", example = "Винтажные часы Rolex", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Название лота не может быть пустым")
        @Size(max = 255, message = "Название не может превышать 255 символов")
        String title,

        @Schema(description = "Подробное описание лота")
        @Size(max = 5000, message = "Описание не может превышать 5000 символов")
        String description,

        @Schema(description = "Начальная цена", example = "100.00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Начальная цена не может быть пустой")
        @DecimalMin(value = "0.01", message = "Начальная цена должна быть больше 0")
        BigDecimal startingPrice,

        @Schema(description = "Минимальный шаг ставки", example = "10.00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Шаг ставки не может быть пустым")
        @DecimalMin(value = "0.01", message = "Шаг ставки должен быть больше 0")
        BigDecimal bidStep,

        @Schema(description = "Длительность торгов в часах от момента старта", example = "168")
        @Min(value = 1, message = "Длительность должна быть не менее 1 часа")
        @Max(value = 720, message = "Длительность не может превышать 720 часов (30 дней)")
        Integer durationHours
) {}