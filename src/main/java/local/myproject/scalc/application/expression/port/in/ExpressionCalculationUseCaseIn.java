package local.myproject.scalc.application.expression.port.in;

import local.myproject.scalc.application.expression.command.EvaluateExpressionCommand;
import local.myproject.scalc.domain.aggregate.expression.ExpressionUnit;

/**
 * Входной порт разового расчета выражений.
 *
 * @author Evgenii Mironov
 */
public interface ExpressionCalculationUseCaseIn {
    /**
     * Выполняет разовый расчет выражения.
     *
     * @param command команда вычисления выражения
     * @return рассчитанное выражение
     */
    ExpressionUnit calculate(EvaluateExpressionCommand command);
}
