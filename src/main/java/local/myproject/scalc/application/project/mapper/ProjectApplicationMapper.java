package local.myproject.scalc.application.project.mapper;

import local.myproject.scalc.application.project.command.CreateProjectCommand;
import local.myproject.scalc.application.project.command.DeleteProjectCommand;
import local.myproject.scalc.application.project.command.UpdateProjectCommand;
import local.myproject.scalc.application.project.query.FindProjectByIdQuery;
import local.myproject.scalc.application.project.query.FindProjectsQuery;
import local.myproject.scalc.presentation.dto.project.ProjectDto;
import org.springframework.stereotype.Component;

/**
 * Маппер DTO проектов в команды и запросы application-слоя.
 *
 * @author Evgenii Mironov
 */
@Component
public class ProjectApplicationMapper {
    /**
     * Создает запрос на получение проектов пользователя.
     *
     * @param userName имя пользователя
     * @return запрос списка проектов
     */
    public FindProjectsQuery toFindAllQuery(String userName) {
        FindProjectsQuery query = new FindProjectsQuery();
        query.setUserName(userName);
        return query;
    }

    /**
     * Создает команду на создание проекта.
     *
     * @param userName имя пользователя
     * @param request DTO проекта
     * @return команда создания проекта
     */
    public CreateProjectCommand toCreateCommand(String userName, ProjectDto request) {
        CreateProjectCommand command = new CreateProjectCommand();
        command.setUserName(userName);
        command.setName(request.name());
        command.setDescription(request.description());
        return command;
    }

    /**
     * Создает запрос на получение проекта по идентификатору.
     *
     * @param userName имя пользователя
     * @param projectId идентификатор проекта
     * @return запрос получения проекта
     */
    public FindProjectByIdQuery toFindByIdQuery(String userName, int projectId) {
        FindProjectByIdQuery query = new FindProjectByIdQuery();
        query.setUserName(userName);
        query.setProjectId(projectId);
        return query;
    }

    /**
     * Создает команду обновления проекта.
     *
     * @param userName имя пользователя
     * @param projectId идентификатор проекта
     * @param request DTO проекта
     * @return команда обновления проекта
     */
    public UpdateProjectCommand toUpdateCommand(String userName, int projectId, ProjectDto request) {
        UpdateProjectCommand command = new UpdateProjectCommand();
        command.setUserName(userName);
        command.setProjectId(projectId);
        command.setName(request.name());
        command.setDescription(request.description());
        return command;
    }

    /**
     * Создает команду удаления проекта.
     *
     * @param userName имя пользователя
     * @param projectId идентификатор проекта
     * @return команда удаления проекта
     */
    public DeleteProjectCommand toDeleteCommand(String userName, int projectId) {
        DeleteProjectCommand command = new DeleteProjectCommand();
        command.setUserName(userName);
        command.setProjectId(projectId);
        return command;
    }
}
