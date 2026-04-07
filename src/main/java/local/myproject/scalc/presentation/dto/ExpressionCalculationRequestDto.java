package local.myproject.scalc.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на разовый расчет выражения")
public class ExpressionCalculationRequestDto {
    @NotBlank(message = "Выражение не должно быть пустым")
    @Schema(description = "Арифметическое выражение для расчета", example = "2 + 2")
    private String expression;

    @Schema(description = "Тип операнда для нормализации выражения", example = "Default")
    private String typeOfOperand;
}
