package edu.rutmiit.demo.auctioncontract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "users", itemRelation = "user")
@Schema(description = "Информация о пользователе (продавце или покупателе)")
public class UserResponse extends RepresentationModel<UserResponse> {

    @Schema(description = "Уникальный идентификатор пользователя", example = "123")
    private final Long id;

    @Schema(description = "Имя пользователя", example = "john_doe")
    private final String username;

    @Schema(description = "Email пользователя", example = "john.doe@example.com")
    private final String email;

    @Schema(description = "Рейтинг пользователя (доверие системы)", example = "98.5")
    private final Double rating;

    // НОВЫЕ ПОЛЯ для верификации
    @Schema(description = "Верифицирован ли пользователь", example = "true")
    private final Boolean verified;

    @Schema(description = "Уровень риска", example = "LOW", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    private final String riskLevel;

    @Schema(description = "Уровень верификации", example = "BASIC", allowableValues = {"NONE", "BASIC", "FULL"})
    private final String verificationLevel;

    @Schema(description = "Рекомендованное действие", example = "ALLOW", allowableValues = {"ALLOW", "BLOCK", "MANUAL_CHECK"})
    private final String recommendedAction;

    @Schema(description = "Оценка достоверности (0-100)", example = "85")
    private final Integer confidenceScore;

    @Schema(description = "Флаги верификации", example = "[\"SUSPICIOUS_EMAIL\", \"VPN_DETECTED\"]")
    private final java.util.List<String> verificationFlags;
}