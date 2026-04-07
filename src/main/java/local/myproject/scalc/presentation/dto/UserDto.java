package local.myproject.scalc.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Data
@Schema(description = "Пользователь API")
public class UserDto {
    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long userId;

    @Schema(description = "Имя пользователя", example = "john")
    private String userName;

    @Schema(description = "Электронная почта", example = "john@example.com")
    private String email;

    @Schema(description = "Набор ролей пользователя", example = "[\"USER\"]")
    private Set<String> roles;
}
