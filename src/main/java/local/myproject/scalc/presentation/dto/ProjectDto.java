package local.myproject.scalc.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Проект пользователя")
public class ProjectDto {
    @Schema(description = "Идентификатор проекта", example = "10")
    private int projectId;

    @Schema(description = "Название проекта", example = "Мой проект")
    private String name;

    @Schema(description = "Описание проекта", example = "Набор выражений для расчетов")
    private String description;

    @Schema(description = "Идентификатор владельца проекта", example = "1")
    private Long userId;
}
