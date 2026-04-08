package local.myproject.scalc.presentation.controller.expression;

import local.myproject.scalc.application.expression.mapper.ExpressionApplicationMapper;
import local.myproject.scalc.application.expression.port.in.ExpressionCalculationUseCaseIn;
import local.myproject.scalc.presentation.dto.expression.ExpressionCalculationRequestDto;
import local.myproject.scalc.presentation.dto.expression.ExpressionCalculationResponseDto;
import local.myproject.scalc.presentation.mapper.expression.ExpressionCalculationPresentationMapper;
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

/**
 * REST-контроллер разового вычисления выражений.
 *
 * @author Evgenii Mironov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/calculator")
@Tag(name = "Калькулятор", description = "Разовый расчет выражений без сохранения в базе данных")
public class ExpressionCalculationController {
    private final ExpressionCalculationUseCaseIn expressionCalculationUseCase;
    private final ExpressionApplicationMapper expressionApplicationMapper;
    private final ExpressionCalculationPresentationMapper expressionCalculationPresentationMapper;

    /**
     * Выполняет разовый расчет выражения.
     *
     * @param request DTO запроса на расчет
     * @return DTO результата расчета
     */
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
        return expressionCalculationPresentationMapper.toDto(
                expressionCalculationUseCase.calculate(expressionApplicationMapper.toEvaluateCommand(request))
        );
    }
}
