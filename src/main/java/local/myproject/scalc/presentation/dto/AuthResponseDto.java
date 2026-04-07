package local.myproject.scalc.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Ответ после успешной аутентификации")
public class AuthResponseDto {
    @Schema(description = "Токен доступа в формате заглушки под будущий JWT", example = "stub-jwt-for-john")
    private String token;

    @Schema(description = "Тип токена", example = "Bearer")
    private String tokenType;

    @Schema(description = "Время жизни токена в секундах", example = "3600")
    private long expiresIn;

    @Schema(description = "Информация об авторизованном пользователе")
    private UserDto user;
}
