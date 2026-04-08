package local.myproject.scalc.presentation.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO ответа аутентификации для REST API.
 *
 * @author Evgenii Mironov
 * @param token токен доступа
 * @param tokenType тип токена
 * @param expiresIn время жизни токена в секундах
 * @param user информация об авторизованном пользователе
 */
@Schema(description = "Ответ после успешной аутентификации")
public record AuthResponseDto(
        @Schema(description = "Токен доступа в формате заглушки под будущий JWT", example = "stub-jwt-for-john")
        String token,
        @Schema(description = "Тип токена", example = "Bearer")
        String tokenType,
        @Schema(description = "Время жизни токена в секундах", example = "3600")
        long expiresIn,
        @Schema(description = "Информация об авторизованном пользователе")
        UserDto user
) {
}
