package edu.rutmiit.demo.auctioncontract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Стандартный ответ об ошибке (RFC 7807 Problem Details)")
public record ErrorResponse(

        @Schema(description = "HTTP статус-код", example = "404")
        int status,

        @Schema(description = "URI-идентификатор типа ошибки", example = "https://api.example.com/problems/lot-not-found")
        String type,

        @Schema(description = "Краткое название ошибки", example = "Лот не найден")
        String title,

        @Schema(description = "Детальное описание", example = "Лот с id=42 не существует")
        String detail,

        @Schema(description = "URI запроса", example = "/api/lots/42")
        String instance,

        @Schema(description = "Момент ошибки", example = "2026-03-03T10:15:30Z")
        Instant timestamp,

        @Schema(description = "Ошибки по полям")
        List<FieldError> fieldErrors
) {

    @Schema(description = "Ошибка валидации поля")
    public record FieldError(
            @Schema(description = "Имя поля", example = "amount")
            String field,
            @Schema(description = "Отклоненное значение", example = "0")
            Object rejectedValue,
            @Schema(description = "Причина", example = "должно быть больше 0")
            String message
    ) {}
}