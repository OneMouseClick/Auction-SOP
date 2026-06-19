package edu.rutmiit.demo.auctioncontract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Полное обновление лота (PUT). Продавец и статус не меняются.")
public record LotUpdateRequest(

        @Schema(description = "Новое название лота", example = "Винтажные часы Rolex (обновленное описание)")
        @Size(max = 255, message = "Название не может превышать 255 символов")
        String title,

        @Schema(description = "Новое описание лота")
        @Size(max = 5000, message = "Описание не может превышать 5000 символов")
        String description,

        @Schema(description = "Новая начальная цена", example = "120.00")
        @DecimalMin(value = "0.01", message = "Начальная цена должна быть больше 0")
        BigDecimal startingPrice,

        @Schema(description = "Новый шаг ставки", example = "15.00")
        @DecimalMin(value = "0.01", message = "Шаг ставки должен быть больше 0")
        BigDecimal bidStep
) {}