package local.myproject.scalc.domain.aggregate.project;

import local.myproject.scalc.domain.aggregate.expression.ExpressionUnit;
import local.myproject.scalc.domain.aggregate.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Доменная модель проекта пользователя.
 *
 * @author Evgenii Mironov
 */
@Getter
@Setter
public class Project {
    private int projectId;
    private String name;
    private String description;
    private Set<ExpressionUnit> expressionUnits;
    private User user;
}
