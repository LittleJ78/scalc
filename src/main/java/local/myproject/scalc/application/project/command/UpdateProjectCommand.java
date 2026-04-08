package local.myproject.scalc.application.project.command;

import lombok.Data;

/**
 * Команда изменения данных проекта.
 *
 * @author Evgenii Mironov
 */
@Data
public class UpdateProjectCommand {
    private String userName;
    private int projectId;
    private String name;
    private String description;
}
