package local.myproject.scalc.application.expression.command;

import lombok.Data;

/**
 * Команда расчета выражения в рамках проекта без сохранения результата.
 *
 * @author Evgenii Mironov
 */
@Data
public class CalculateProjectExpressionCommand {
    private String userName;
    private int projectId;
    private String typeOfOperand;
    private String expressionUnitName;
    private String defaultExpression;
    private boolean watchList;
}
