package local.myproject.scalc.application.expression.usecase;

import local.myproject.scalc.application.expression.command.EvaluateExpressionCommand;
import local.myproject.scalc.application.expression.port.in.ExpressionCalculationUseCaseIn;
import local.myproject.scalc.application.expression.port.out.ExpressionCalculatorPort;
import local.myproject.scalc.domain.aggregate.expression.ExpressionUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Use case разового расчета выражений без сохранения.
 *
 * @author Evgenii Mironov
 */
@Service
@RequiredArgsConstructor
public class ExpressionCalculationUseCase implements ExpressionCalculationUseCaseIn {
    private final ExpressionCalculatorPort expressionCalculatorPort;

    /**
     * Выполняет расчет выражения по входной команде.
     *
     * @param command команда вычисления выражения
     * @return рассчитанное выражение
     */
    public ExpressionUnit calculate(EvaluateExpressionCommand command) {
        ExpressionUnit expressionUnit = new ExpressionUnit(defaultOperandType(command.getTypeOfOperand()));
        expressionUnit.setExpressionUnitName("Temporary_Value");
        expressionUnit.setDefaultExpression(command.getExpression() == null ? "" : command.getExpression());
        return expressionCalculatorPort.calculate(expressionUnit, new String[0][0]);
    }

    /**
     * Возвращает тип операнда по умолчанию, если значение не задано.
     *
     * @param typeOfOperand тип операнда
     * @return тип операнда
     */
    private String defaultOperandType(String typeOfOperand) {
        return typeOfOperand == null || typeOfOperand.isBlank() ? "Default" : typeOfOperand;
    }
}
