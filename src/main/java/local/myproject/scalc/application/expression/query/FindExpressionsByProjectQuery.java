package local.myproject.scalc.application.expression.query;

import lombok.Data;

/**
 * Запрос на получение списка выражений проекта.
 *
 * @author Evgenii Mironov
 */
@Data
public class FindExpressionsByProjectQuery {
    private String userName;
    private int projectId;
}
