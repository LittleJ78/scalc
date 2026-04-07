package local.myproject.scalc.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Результат разового расчета выражения")
public class ExpressionCalculationResponseDto {
    @Schema(description = "Исходное выражение", example = "2 + 2")
    private String expression;

    @Schema(description = "Нормализованное вычисленное выражение", example = "2 + 2 = 4")
    private String unitExpression;

    @Schema(description = "Итоговый результат вычисления", example = "4")
    private String result;

    @Schema(description = "Список атомарных действий, из которых состоит вычисление")
    private List<String> atomicActions;
}
