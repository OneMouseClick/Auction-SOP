package edu.rutmiit.demo.auctioncontract.endpoints;

import edu.rutmiit.demo.auctioncontract.config.AuctionContractConfig;
import edu.rutmiit.demo.auctioncontract.dto.ErrorResponse;
import edu.rutmiit.demo.auctioncontract.dto.UserRegistrationRequest;
import edu.rutmiit.demo.auctioncontract.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "Управление пользователями")
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public interface UserApi {

    @Operation(summary = "Зарегистрировать пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь создан. Отправлен на верификацию.")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Пользователь с таким именем или email уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<UserResponse>> registerUser(
            @Valid @RequestBody UserRegistrationRequest request,
            HttpServletRequest httpRequest
    );

    @Operation(summary = "Получить пользователя по ID",
            security = @SecurityRequirement(name = AuctionContractConfig.SECURITY_SCHEME_BEARER))
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    EntityModel<UserResponse> getUserById(
            @PathVariable Long id
    );
}