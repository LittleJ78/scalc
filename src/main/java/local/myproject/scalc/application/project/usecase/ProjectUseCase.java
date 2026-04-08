package local.myproject.scalc.application.project.usecase;

import local.myproject.scalc.application.project.command.CreateProjectCommand;
import local.myproject.scalc.application.project.command.DeleteProjectCommand;
import local.myproject.scalc.application.project.command.UpdateProjectCommand;
import local.myproject.scalc.application.project.port.in.ProjectUseCaseIn;
import local.myproject.scalc.application.project.port.out.ProjectPort;
import local.myproject.scalc.application.project.query.FindProjectByIdQuery;
import local.myproject.scalc.application.project.query.FindProjectsQuery;
import local.myproject.scalc.application.user.port.out.UserPort;
import local.myproject.scalc.domain.aggregate.project.Project;
import local.myproject.scalc.domain.aggregate.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Use case управления проектами пользователя.
 *
 * @author Evgenii Mironov
 */
@Service
@RequiredArgsConstructor
public class ProjectUseCase implements ProjectUseCaseIn {
    private final ProjectPort projectPort;
    private final UserPort userPort;

    /**
     * Возвращает список проектов пользователя.
     *
     * @param query запрос списка проектов
     * @return список проектов
     */
    public List<Project> findAll(FindProjectsQuery query) {
        User user = getCurrentUser(query.getUserName());
        return projectPort.findAllByUser(user.getUserId());
    }

    /**
     * Создает новый проект пользователя.
     *
     * @param command команда создания проекта
     * @return созданный проект
     */
    public Project create(CreateProjectCommand command) {
        User user = getCurrentUser(command.getUserName());
        Project project = new Project();
        project.setName(command.getName());
        project.setDescription(command.getDescription());
        project.setUser(user);
        projectPort.save(project);
        return project;
    }

    /**
     * Возвращает проект по идентификатору.
     *
     * @param query запрос получения проекта
     * @return найденный проект
     */
    public Project findById(FindProjectByIdQuery query) {
        return getOwnedProject(query.getUserName(), query.getProjectId());
    }

    /**
     * Обновляет существующий проект.
     *
     * @param command команда обновления проекта
     * @return обновленный проект
     */
    public Project update(UpdateProjectCommand command) {
        Project project = getOwnedProject(command.getUserName(), command.getProjectId());
        project.setName(command.getName());
        project.setDescription(command.getDescription());
        projectPort.update(project);
        return project;
    }

    /**
     * Удаляет проект пользователя.
     *
     * @param command команда удаления проекта
     * @return результат не возвращается
     */
    public void delete(DeleteProjectCommand command) {
        getOwnedProject(command.getUserName(), command.getProjectId());
        projectPort.deleteById(command.getProjectId());
    }

    /**
     * Возвращает проект, принадлежащий пользователю.
     *
     * @param userName имя пользователя
     * @param projectId идентификатор проекта
     * @return проект пользователя
     */
    public Project getOwnedProject(String userName, int projectId) {
        User user = getCurrentUser(userName);
        Project project = projectPort.findById(projectId);
        if (project.getUser() == null || !project.getUser().getUserId().equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        return project;
    }

    /**
     * Возвращает пользователя по имени.
     *
     * @param userName имя пользователя
     * @return найденный пользователь
     */
    private User getCurrentUser(String userName) {
        return userPort.findByUserName(userName);
    }
}
