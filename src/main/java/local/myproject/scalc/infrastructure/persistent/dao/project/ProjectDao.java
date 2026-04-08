package local.myproject.scalc.infrastructure.persistent.dao.project;

import local.myproject.scalc.domain.aggregate.project.Project;

import java.util.List;
import java.util.Optional;

/**
 * DAO для работы с проектами.
 *
 * @author Evgenii Mironov
 */
public interface ProjectDao {
    /**
     * Возвращает проекты пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список проектов
     */
    List<Project> findAllByUserId(Long userId);

    /**
     * Ищет проект по идентификатору.
     *
     * @param projectId идентификатор проекта
     * @return найденный проект или пустой результат
     */
    Optional<Project> findById(int projectId);

    /**
     * Сохраняет проект.
     *
     * @param project проект для сохранения
     * @return сохраненный проект
     */
    Project save(Project project);

    /**
     * Обновляет проект.
     *
     * @param project проект для обновления
     * @return обновленный проект
     */
    Project update(Project project);

    /**
     * Удаляет проект по идентификатору.
     *
     * @param projectId идентификатор проекта
     * @return результат не возвращается
     */
    void deleteById(int projectId);
}
