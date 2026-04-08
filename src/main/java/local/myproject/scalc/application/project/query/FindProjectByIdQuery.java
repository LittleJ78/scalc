package local.myproject.scalc.application.project.query;

import lombok.Data;

/**
 * Запрос на получение проекта по идентификатору.
 *
 * @author Evgenii Mironov
 */
@Data
public class FindProjectByIdQuery {
    private String userName;
    private int projectId;
}
