package local.myproject.scalc.presentation.dto.expression;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * DTO выражения в REST API.
 *
 * @author Evgenii Mironov
 * @param expressionUnitId идентификатор выражения
 * @param typeOfOperand тип операнда
 * @param expressionUnitName имя выражения
 * @param defaultExpression исходное выражение
 * @param expressionResult результат вычисления
 * @param projectId идентификатор проекта
 * @param watchList признак отображения в списке наблюдения
 * @param unitExpression нормализованное вычисленное выражение
 * @param atomicExpressions список атомарных операций выражения
 */
@Schema(description = "Выражение внутри проекта")
public record ExpressionUnitDto(
        @Schema(description = "Идентификатор выражения", example = "15")
        int expressionUnitId,
        @Schema(description = "Тип операнда", example = "Default")
        String typeOfOperand,
        @Schema(description = "Имя выражения", example = "Total_Value")
        String expressionUnitName,
        @Schema(description = "Исходное выражение", example = "2 + 2")
        String defaultExpression,
        @Schema(description = "Результат вычисления", example = "4")
        String expressionResult,
        @Schema(description = "Идентификатор проекта", example = "10")
        Integer projectId,
        @Schema(description = "Признак отображения в списке наблюдения", example = "true")
        boolean watchList,
        @Schema(description = "Нормализованное вычисленное выражение", example = "2 + 2 = 4")
        String unitExpression,
        @Schema(description = "Список атомарных операций выражения")
        List<String> atomicExpressions
) {
}
