package local.myproject.scalc.presentation.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO запроса на регистрацию пользователя.
 *
 * @author Evgenii Mironov
 * @param userName имя пользователя
 * @param password пароль
 * @param confirmPassword подтверждение пароля
 * @param email электронная почта
 */
@Schema(description = "Запрос на регистрацию пользователя")
public record RegisterRequestDto(
        @Schema(description = "Имя пользователя", example = "john")
        String userName,
        @Schema(description = "Пароль", example = "secret123")
        String password,
        @Schema(description = "Подтверждение пароля", example = "secret123")
        String confirmPassword,
        @Schema(description = "Электронная почта", example = "john@example.com")
        String email
) {
}
