package local.myproject.scalc.application.expression.command;

import lombok.Data;

/**
 * Команда создания нового выражения в проекте.
 *
 * @author Evgenii Mironov
 */
@Data
public class CreateExpressionCommand {
    private String userName;
    private int projectId;
    private String typeOfOperand;
    private String expressionUnitName;
    private String defaultExpression;
    private boolean watchList;
}
