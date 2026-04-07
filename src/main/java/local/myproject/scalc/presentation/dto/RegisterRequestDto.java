package local.myproject.scalc.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию пользователя")
public class RegisterRequestDto {
    @Schema(description = "Имя пользователя", example = "john")
    private String userName;

    @Schema(description = "Пароль", example = "secret123")
    private String password;

    @Schema(description = "Подтверждение пароля", example = "secret123")
    private String confirmPassword;

    @Schema(description = "Электронная почта", example = "john@example.com")
    private String email;
}
