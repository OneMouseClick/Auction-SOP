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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Bids", description = "Управление ставками")
@RequestMapping(value = "/api/bids", produces = MediaType.APPLICATION_JSON_VALUE)
public interface BidApi {

    @Operation(summary = "Получить ставку по ID", security = @SecurityRequirement(name = AuctionContractConfig.SECURITY_SCHEME_BEARER))
    @ApiResponse(responseCode = "200", description = "Ставка найдена")
    @ApiResponse(responseCode = "404", description = "Ставка не найдена", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    EntityModel<BidResponse> getBidById(
            @Parameter(description = "ID ставки", required = true, example = "500") @PathVariable Long id
    );

    @Operation(summary = "Сделать ставку", description = "Размещает новую ставку на указанный лот.", security = @SecurityRequirement(name = AuctionContractConfig.SECURITY_SCHEME_BEARER))
    @ApiResponse(responseCode = "201", description = "Ставка принята")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Лот или покупатель не найден", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Лот не активен или ставка слишком мала", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(value = "/lot/{lotId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<BidResponse>> placeBid(
            @Parameter(description = "ID лота", required = true, example = "1") @PathVariable Long lotId,
            @Valid @RequestBody BidPlaceRequest request
    );
}