package local.myproject.scalc.infrastructure.calculation;

import local.myproject.calculate.converter.Converter;
import local.myproject.calculate.Expression;
import local.myproject.calculate.operator.TypeOfOperands;
import local.myproject.calculate.evaluator.EvaluationResult;
import local.myproject.scalc.application.expression.port.out.ExpressionCalculatorPort;
import local.myproject.scalc.domain.aggregate.expression.ExpressionUnit;
import org.springframework.stereotype.Component;

/**
 * Адаптер legacy-движка вычислений к текущей архитектуре приложения.
 *
 * @author Evgenii Mironov
 */
@Component
public class LegacyCalculationEngineAdapter implements ExpressionCalculatorPort {
    /**
     * Вычисляет выражение, нормализует его и заполняет результаты расчета.
     *
     * @param expressionUnit выражение для расчета
     * @param parameters набор параметров проекта в формате имя-значение
     * @return рассчитанное выражение
     */
    @Override
    public ExpressionUnit calculate(ExpressionUnit expressionUnit, String[][] parameters) {
        try {
            String sourceExpression = expressionUnit.getDefaultExpression() == null ? "" : expressionUnit.getDefaultExpression();
            Expression expression = createExpression(expressionUnit, parameters, sourceExpression);
            EvaluationResult evaluationResult = expression.evaluate();

            expressionUnit.setDefaultExpression(sourceExpression.isBlank() ? "" : expression.normaliseExpr(sourceExpression));
            expressionUnit.setExpressionResult(Converter.operandToString(evaluationResult.operand()));
            expressionUnit.setUnitExpression(
                    expression.getUnitExpression().equals("0")
                            ? ""
                            : expression.getUnitExpression() + " = " + expressionUnit.getExpressionResult()
            );
            expressionUnit.setAtomicExpressions(expression.getAtomicActions(evaluationResult));
            return expressionUnit;
        } catch (Exception exception) {
            throw new IllegalStateException("Не удалось вычислить выражение", exception);
        }
    }

    /**
     * Создает объект выражения legacy-движка для вычисления.
     *
     * @param expressionUnit выражение приложения
     * @param parameters набор параметров проекта
     * @param sourceExpression исходное выражение
     * @return объект legacy-движка
     * @throws Exception если выражение не удалось подготовить
     */
    private Expression createExpression(ExpressionUnit expressionUnit, String[][] parameters, String sourceExpression) throws Exception {
        if (sourceExpression.isBlank()) {
            return new Expression("0");
        }
        if ("Default".equals(expressionUnit.getTypeOfOperand())) {
            return new Expression(sourceExpression, parameters);
        }
        Expression baseExpression = new Expression(sourceExpression, parameters);
        return new Expression(
                baseExpression.getUnitExpression(TypeOfOperands.valueOf(expressionUnit.getTypeOfOperand())),
                parameters
        );
    }
}
