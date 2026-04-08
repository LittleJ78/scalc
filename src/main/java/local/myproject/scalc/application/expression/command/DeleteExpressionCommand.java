package local.myproject.scalc.application.expression.command;

import lombok.Data;

/**
 * Команда удаления выражения по идентификатору.
 *
 * @author Evgenii Mironov
 */
@Data
public class DeleteExpressionCommand {
    private String userName;
    private int expressionUnitId;
}
