package local.myproject.scalc.application.project.query;

import lombok.Data;

/**
 * Запрос на получение списка проектов пользователя.
 *
 * @author Evgenii Mironov
 */
@Data
public class FindProjectsQuery {
    private String userName;
}
