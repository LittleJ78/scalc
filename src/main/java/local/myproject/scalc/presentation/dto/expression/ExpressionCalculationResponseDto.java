package local.myproject.scalc.presentation.dto.expression;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * DTO ответа с результатом разового расчета выражения.
 *
 * @author Evgenii Mironov
 * @param expression исходное выражение
 * @param unitExpression нормализованное вычисленное выражение
 * @param result итоговый результат вычисления
 * @param atomicActions список атомарных действий
 */
@Schema(description = "Результат разового расчета выражения")
public record ExpressionCalculationResponseDto(
        @Schema(description = "Исходное выражение", example = "2 + 2")
        String expression,
        @Schema(description = "Нормализованное вычисленное выражение", example = "2 + 2 = 4")
        String unitExpression,
        @Schema(description = "Итоговый результат вычисления", example = "4")
        String result,
        @Schema(description = "Список атомарных действий, из которых состоит вычисление")
        List<String> atomicActions
) {
}
