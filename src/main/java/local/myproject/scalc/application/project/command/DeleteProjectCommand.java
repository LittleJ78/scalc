package local.myproject.scalc.application.project.command;

import lombok.Data;

/**
 * Команда удаления проекта пользователя.
 *
 * @author Evgenii Mironov
 */
@Data
public class DeleteProjectCommand {
    private String userName;
    private int projectId;
}
