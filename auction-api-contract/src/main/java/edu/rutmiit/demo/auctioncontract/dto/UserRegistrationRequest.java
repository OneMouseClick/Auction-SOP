package edu.rutmiit.demo.auctioncontract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на регистрацию пользователя")
public record UserRegistrationRequest(

        @Schema(description = "Имя пользователя", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя пользователя не может быть пустым")
        @Size(min = 3, max = 50, message = "Имя пользователя должно содержать от 3 до 50 символов")
        String username,

        @Schema(description = "Email пользователя", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Некорректный формат email")
        @Size(max = 255, message = "Email не может превышать 255 символов")
        String email,

        @Schema(description = "Пароль пользователя", example = "securePassword123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 100, message = "Пароль должен содержать от 8 до 100 символов")
        String password,

        @Schema(description = "Телефон пользователя", example = "+79991234567")
        @Size(max = 20, message = "Телефон не может превышать 20 символов")
        String phone
) {}