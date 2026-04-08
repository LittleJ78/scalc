package local.myproject.scalc.application.expression.command;

import lombok.Data;

/**
 * Команда разового вычисления выражения без привязки к проекту.
 *
 * @author Evgenii Mironov
 */
@Data
public class EvaluateExpressionCommand {
    private String expression;
    private String typeOfOperand;
}
