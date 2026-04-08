package local.myproject.scalc.application.expression.usecase;

import local.myproject.scalc.application.expression.command.EvaluateExpressionCommand;
import local.myproject.scalc.application.expression.port.out.ExpressionCalculatorPort;
import local.myproject.scalc.domain.aggregate.expression.ExpressionUnit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExpressionCalculationUseCaseTest {

    @Test
    void calculateDelegatesToCalculatorPortAndReturnsCalculatedExpression() {
        ExpressionCalculatorPort expressionCalculatorPort = (expressionUnit, parameters) -> {
            expressionUnit.setExpressionResult("4");
            expressionUnit.setUnitExpression("2 + 2 = 4");
            expressionUnit.setAtomicExpressions(List.of("2 + 2 = 4"));
            return expressionUnit;
        };
        ExpressionCalculationUseCase expressionCalculationUseCase = new ExpressionCalculationUseCase(expressionCalculatorPort);

        EvaluateExpressionCommand command = new EvaluateExpressionCommand();
        command.setExpression("2 + 2");
        command.setTypeOfOperand("Default");

        ExpressionUnit response = expressionCalculationUseCase.calculate(command);

        assertEquals("2 + 2", response.getDefaultExpression());
        assertEquals("4", response.getExpressionResult());
        assertEquals("2 + 2 = 4", response.getUnitExpression());
        assertNotNull(response.getAtomicExpressions());
        assertFalse(response.getAtomicExpressions().isEmpty());
    }
}
