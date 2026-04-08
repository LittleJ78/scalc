package local.myproject.scalc.application.expression.query;

import lombok.Data;

/**
 * Запрос на получение выражения по идентификатору.
 *
 * @author Evgenii Mironov
 */
@Data
public class FindExpressionByIdQuery {
    private String userName;
    private int expressionUnitId;
}
