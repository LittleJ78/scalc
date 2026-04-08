package local.myproject.scalc.application.project.command;

import lombok.Data;

/**
 * Команда создания проекта пользователя.
 *
 * @author Evgenii Mironov
 */
@Data
public class CreateProjectCommand {
    private String userName;
    private String name;
    private String description;
}
