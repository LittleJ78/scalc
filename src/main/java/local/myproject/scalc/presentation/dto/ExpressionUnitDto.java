package local.myproject.scalc.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Выражение внутри проекта")
public class ExpressionUnitDto {
    @Schema(description = "Идентификатор выражения", example = "15")
    private int expressionUnitId;

    @Schema(description = "Тип операнда", example = "Default")
    private String typeOfOperand;

    @Schema(description = "Имя выражения", example = "Total_Value")
    private String expressionUnitName;

    @Schema(description = "Исходное выражение", example = "2 + 2")
    private String defaultExpression;

    @Schema(description = "Результат вычисления", example = "4")
    private String expressionResult;

    @Schema(description = "Идентификатор проекта", example = "10")
    private Integer projectId;

    @Schema(description = "Признак отображения в списке наблюдения", example = "true")
    private boolean watchList;

    @Schema(description = "Нормализованное вычисленное выражение", example = "2 + 2 = 4")
    private String unitExpression;

    @Schema(description = "Список атомарных операций выражения")
    private List<String> atomicExpressions;
}
