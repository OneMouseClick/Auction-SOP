package edu.rutmiit.demo.auctioncontract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Частичное обновление лота (PATCH). Передаются только те поля, которые нужно изменить.")
public record PatchLotRequest(

        @Schema(description = "Новое название лота", example = "Винтажные часы Rolex (новое название)")
        @Size(max = 255, message = "Название не может превышать 255 символов")
        String title,

        @Schema(description = "Новое описание лота")
        @Size(max = 5000, message = "Описание не может превышать 5000 символов")
        String description,

        @Schema(description = "Новый шаг ставки", example = "15.00")
        @DecimalMin(value = "0.01", message = "Шаг ставки должен быть больше 0")
        BigDecimal bidStep
) {}