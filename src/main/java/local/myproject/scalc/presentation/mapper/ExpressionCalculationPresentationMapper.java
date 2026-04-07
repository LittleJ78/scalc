package local.myproject.scalc.presentation.mapper;

import local.myproject.scalc.domain.ExpressionUnit;
import local.myproject.scalc.presentation.dto.ExpressionCalculationRequestDto;
import local.myproject.scalc.presentation.dto.ExpressionCalculationResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ExpressionCalculationPresentationMapper {
    public ExpressionUnit toDomain(ExpressionCalculationRequestDto dto) {
        ExpressionUnit expressionUnit = new ExpressionUnit(defaultOperandType(dto.getTypeOfOperand()));
        expressionUnit.setExpressionUnitName("Temporary_Value");
        try {
            expressionUnit.setDefaultExpression(dto.getExpression() == null ? "" : dto.getExpression());
        } catch (Exception exception) {
            throw new IllegalStateException("Не удалось подготовить выражение к расчету", exception);
        }
        return expressionUnit;
    }

    public ExpressionCalculationResponseDto toDto(ExpressionUnit expressionUnit) {
        ExpressionCalculationResponseDto dto = new ExpressionCalculationResponseDto();
        dto.setExpression(expressionUnit.getDefaultExpression());
        try {
            dto.setUnitExpression(expressionUnit.getUnitExpression());
            dto.setResult(expressionUnit.getExpressionResult());
            dto.setAtomicActions(expressionUnit.getAtomicExpressions());
        } catch (Exception exception) {
            throw new IllegalStateException("Не удалось подготовить ответ расчета", exception);
        }
        return dto;
    }

    private String defaultOperandType(String typeOfOperand) {
        return typeOfOperand == null || typeOfOperand.isBlank() ? "Default" : typeOfOperand;
    }
}
