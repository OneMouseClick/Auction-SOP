package edu.rutmiit.demo.auctioncontract.endpoints;

import edu.rutmiit.demo.auctioncontract.config.AuctionContractConfig;
import edu.rutmiit.demo.auctioncontract.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Lots", description = "Управление лотами аукциона")
@RequestMapping(value = "/api/lots", produces = MediaType.APPLICATION_JSON_VALUE)
public interface LotApi {

    @Operation(summary = "Список лотов", description = "Постраничный список лотов с фильтрацией по статусу.")
    @ApiResponse(responseCode = "200", description = "Список лотов")
    @GetMapping
    PagedModel<EntityModel<LotResponse>> getAllLots(
            @Parameter(description = "Фильтр по статусу") @RequestParam(required = false) LotStatus status,
            @Parameter(description = "ID продавца") @RequestParam(required = false) Long sellerId,
            @Parameter(description = "Номер страницы", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );

    @Operation(summary = "Получить лот по ID", security = @SecurityRequirement(name = AuctionContractConfig.SECURITY_SCHEME_BEARER))
    @ApiResponse(responseCode = "200", description = "Лот найден")
    @ApiResponse(responseCode = "404", description = "Лот не найден", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    EntityModel<LotResponse> getLotById(
            @Parameter(description = "ID лота", required = true, example = "1") @PathVariable Long id
    );

    @Operation(summary = "Создать лот", security = @SecurityRequirement(name = AuctionContractConfig.SECURITY_SCHEME_BEARER))
    @ApiResponse(responseCode = "201", description = "Лот создан и отправлен на проверку. Статус: PENDING_VERIFICATION")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Продавец с указанным ID не найден", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<LotResponse>> createLot(@Valid @RequestBody LotCreateRequest request);

    @Operation(summary = "Обновить лот (PUT)", description = "Полное обновление. Статус и продавец не меняются.", security = @SecurityRequirement(name = AuctionContractConfig.SECURITY_SCHEME_BEARER))
    @ApiResponse(responseCode = "200", description = "Лот обновлен")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Лот не найден", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<LotResponse> updateLot(
            @Parameter(description = "ID лота", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody LotUpdateRequest request
    );

    @Operation(summary = "Частичное обновление лота (PATCH)", description = "Обновляет только переданные поля.", security = @SecurityRequirement(name = AuctionContractConfig.SECURITY_SCHEME_BEARER))
    @ApiResponse(responseCode = "200", description = "Лот обновлен")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Лот не найден", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<LotResponse> patchLot(
            @Parameter(description = "ID лота", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody PatchLotRequest request
    );

    @Operation(summary = "Удалить лот", description = "Удаляет лот (возможно, только если он в статусе PENDING или CANCELLED).", security = @SecurityRequirement(name = AuctionContractConfig.SECURITY_SCHEME_BEARER))
    @ApiResponse(responseCode = "204", description = "Лот удален")
    @ApiResponse(responseCode = "404", description = "Лот не найден", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Нельзя удалить активный лот", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLot(
            @Parameter(description = "ID лота", required = true, example = "1") @PathVariable Long id
    );

    @Operation(summary = "Ставки по лоту", description = "Список ставок, сделанных по данному лоту.")
    @ApiResponse(responseCode = "200", description = "Список ставок")
    @ApiResponse(responseCode = "404", description = "Лот не найден", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}/bids")
    PagedModel<EntityModel<BidResponse>> getBidsForLot(
            @Parameter(description = "ID лота", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Номер страницы", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );
}