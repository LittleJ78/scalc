package local.myproject.scalc.presentation.dto.expression;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO запроса на разовый расчет выражения.
 *
 * @author Evgenii Mironov
 * @param expression арифметическое выражение для расчета
 * @param typeOfOperand тип операнда для нормализации выражения
 */
@Schema(description = "Запрос на разовый расчет выражения")
public record ExpressionCalculationRequestDto(
        @NotBlank(message = "Выражение не должно быть пустым")
        @Schema(description = "Арифметическое выражение для расчета", example = "2 + 2")
        String expression,
        @Schema(description = "Тип операнда для нормализации выражения", example = "Default")
        String typeOfOperand
) {
}
