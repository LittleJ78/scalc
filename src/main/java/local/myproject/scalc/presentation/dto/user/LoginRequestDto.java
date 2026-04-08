package local.myproject.scalc.presentation.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO запроса на вход пользователя.
 *
 * @author Evgenii Mironov
 * @param userName имя пользователя
 * @param password пароль пользователя
 */
@Schema(description = "Запрос на вход в систему")
public record LoginRequestDto(
        @Schema(description = "Имя пользователя", example = "john")
        String userName,
        @Schema(description = "Пароль пользователя", example = "secret123")
        String password
) {
}
