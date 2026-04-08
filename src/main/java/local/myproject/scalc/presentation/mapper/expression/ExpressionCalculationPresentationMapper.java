package local.myproject.scalc.presentation.mapper.expression;

import local.myproject.scalc.domain.aggregate.expression.ExpressionUnit;
import local.myproject.scalc.presentation.dto.expression.ExpressionCalculationRequestDto;
import local.myproject.scalc.presentation.dto.expression.ExpressionCalculationResponseDto;
import org.springframework.stereotype.Component;

/**
 * Маппер разового расчета выражений между DTO и доменной моделью.
 *
 * @author Evgenii Mironov
 */
@Component
public class ExpressionCalculationPresentationMapper {
    /**
     * Преобразует DTO запроса в доменную модель выражения.
     *
     * @param dto DTO запроса на расчет
     * @return доменная модель выражения
     */
    public ExpressionUnit toDomain(ExpressionCalculationRequestDto dto) {
        ExpressionUnit expressionUnit = new ExpressionUnit(defaultOperandType(dto.typeOfOperand()));
        expressionUnit.setExpressionUnitName("Temporary_Value");
        try {
            expressionUnit.setDefaultExpression(dto.expression() == null ? "" : dto.expression());
        } catch (Exception exception) {
            throw new IllegalStateException("Не удалось подготовить выражение к расчету", exception);
        }
        return expressionUnit;
    }

    /**
     * Преобразует доменное выражение в DTO результата расчета.
     *
     * @param expressionUnit доменная модель выражения
     * @return DTO результата расчета
     */
    public ExpressionCalculationResponseDto toDto(ExpressionUnit expressionUnit) {
        try {
            return new ExpressionCalculationResponseDto(
                    expressionUnit.getDefaultExpression(),
                    expressionUnit.getUnitExpression(),
                    expressionUnit.getExpressionResult(),
                    expressionUnit.getAtomicExpressions()
            );
        } catch (Exception exception) {
            throw new IllegalStateException("Не удалось подготовить ответ расчета", exception);
        }
    }

    /**
     * Возвращает тип операнда по умолчанию.
     *
     * @param typeOfOperand тип операнда
     * @return итоговый тип операнда
     */
    private String defaultOperandType(String typeOfOperand) {
        return typeOfOperand == null || typeOfOperand.isBlank() ? "Default" : typeOfOperand;
    }
}
