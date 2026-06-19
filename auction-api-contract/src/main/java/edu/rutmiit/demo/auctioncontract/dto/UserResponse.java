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
}