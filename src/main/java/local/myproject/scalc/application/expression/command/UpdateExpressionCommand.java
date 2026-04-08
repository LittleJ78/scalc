package local.myproject.scalc.application.expression.command;

import lombok.Data;

/**
 * Команда обновления существующего выражения.
 *
 * @author Evgenii Mironov
 */
@Data
public class UpdateExpressionCommand {
    private String userName;
    private int expressionUnitId;
    private String typeOfOperand;
    private String expressionUnitName;
    private String defaultExpression;
    private boolean watchList;
}
