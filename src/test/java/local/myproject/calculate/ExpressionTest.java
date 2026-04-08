package local.myproject.calculate;

import local.myproject.calculate.converter.Converter;
import local.myproject.calculate.evaluator.EvaluationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpressionTest {

    @Test
    void evaluatesExpressionAndExposesAtomicActionsWithoutMutation() throws Exception {
        Expression expression = new Expression("2+2*2");
        EvaluationResult result = expression.evaluate();

        assertEquals("2 + 2 * 2", expression.getUnitExpression());
        assertEquals("6", Converter.operandToString(result.operand()));
        assertEquals(2, result.atomicExpressions().size());
        assertEquals("4", Converter.operandToString(result.atomicExpressions().get(0).result()));
        assertEquals("2 * 2", expression.getAtomicExpression(0));
        assertEquals("2 + 4", expression.getAtomicExpression(1));
    }

    @Test
    void replacesParametersDuringCalculation() throws Exception {
        Expression expression = new Expression("A+1", new String[]{"A", "2"});

        assertEquals("A + 1", expression.getUnitExpression());
        assertEquals("3", Converter.operandToString(expression.calculate()));
    }

    @Test
    void throwsWhenAtomicExpressionIndexIsOutOfBounds() throws Exception {
        Expression expression = new Expression("1+1");
        expression.calculate();

        assertThrows(IndexOutOfBoundsException.class, () -> expression.getAtomicExpression(10));
    }
}
