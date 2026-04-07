package local.myproject.scalc.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на вход в систему")
public class LoginRequestDto {
    @Schema(description = "Имя пользователя", example = "john")
    private String userName;

    @Schema(description = "Пароль пользователя", example = "secret123")
    private String password;
}
