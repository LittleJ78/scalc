package local.myproject.scalc.presentation.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

/**
 * DTO пользователя в REST API.
 *
 * @author Evgenii Mironov
 * @param userId идентификатор пользователя
 * @param userName имя пользователя
 * @param email электронная почта
 * @param roles набор ролей пользователя
 */
@Schema(description = "Пользователь API")
public record UserDto(
        @Schema(description = "Идентификатор пользователя", example = "1")
        Long userId,
        @Schema(description = "Имя пользователя", example = "john")
        String userName,
        @Schema(description = "Электронная почта", example = "john@example.com")
        String email,
        @Schema(description = "Набор ролей пользователя", example = "[\"USER\"]")
        Set<String> roles
) {
}
