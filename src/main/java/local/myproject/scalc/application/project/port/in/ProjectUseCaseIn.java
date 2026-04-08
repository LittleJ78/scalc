package local.myproject.scalc.application.project.port.in;

import local.myproject.scalc.application.project.command.CreateProjectCommand;
import local.myproject.scalc.application.project.command.DeleteProjectCommand;
import local.myproject.scalc.application.project.command.UpdateProjectCommand;
import local.myproject.scalc.application.project.query.FindProjectByIdQuery;
import local.myproject.scalc.application.project.query.FindProjectsQuery;
import local.myproject.scalc.domain.aggregate.project.Project;

import java.util.List;

/**
 * Входной порт сценариев управления проектами.
 *
 * @author Evgenii Mironov
 */
public interface ProjectUseCaseIn {
    /**
     * Возвращает список проектов пользователя.
     *
     * @param query запрос списка проектов
     * @return список проектов
     */
    List<Project> findAll(FindProjectsQuery query);

    /**
     * Создает проект пользователя.
     *
     * @param command команда создания проекта
     * @return созданный проект
     */
    Project create(CreateProjectCommand command);

    /**
     * Возвращает проект по идентификатору.
     *
     * @param query запрос получения проекта
     * @return найденный проект
     */
    Project findById(FindProjectByIdQuery query);

    /**
     * Обновляет проект пользователя.
     *
     * @param command команда обновления проекта
     * @return обновленный проект
     */
    Project update(UpdateProjectCommand command);

    /**
     * Удаляет проект пользователя.
     *
     * @param command команда удаления проекта
     * @return результат не возвращается
     */
    void delete(DeleteProjectCommand command);
}
