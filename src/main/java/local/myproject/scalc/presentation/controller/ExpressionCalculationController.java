package local.myproject.scalc.presentation.controller;

import local.myproject.scalc.presentation.dto.ExpressionCalculationRequestDto;
import local.myproject.scalc.presentation.dto.ExpressionCalculationResponseDto;
import local.myproject.scalc.services.ExpressionCalculationApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/calculator")
@Tag(name = "Калькулятор", description = "Разовый расчет выражений без сохранения в базе данных")
public class ExpressionCalculationController {
    private final ExpressionCalculationApiService expressionCalculationApiService;

    @PostMapping("/evaluate")
    @Operation(
            summary = "Рассчитать выражение",
            description = "Принимает арифметическое выражение и возвращает итоговый результат вместе с атомарными действиями"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Выражение успешно рассчитано"),
            @ApiResponse(responseCode = "400", description = "Передан некорректный запрос")
    })
    public ExpressionCalculationResponseDto calculate(@Valid @RequestBody ExpressionCalculationRequestDto request) {
        return expressionCalculationApiService.calculate(request);
    }
}
