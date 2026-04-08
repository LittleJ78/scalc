package local.myproject.scalc.presentation.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO проекта в REST API.
 *
 * @author Evgenii Mironov
 * @param projectId идентификатор проекта
 * @param name название проекта
 * @param description описание проекта
 * @param userId идентификатор владельца проекта
 */
@Schema(description = "Проект пользователя")
public record ProjectDto(
        @Schema(description = "Идентификатор проекта", example = "10")
        int projectId,
        @Schema(description = "Название проекта", example = "Мой проект")
        String name,
        @Schema(description = "Описание проекта", example = "Набор выражений для расчетов")
        String description,
        @Schema(description = "Идентификатор владельца проекта", example = "1")
        Long userId
) {
}
