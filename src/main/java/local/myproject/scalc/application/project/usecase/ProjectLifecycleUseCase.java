package local.myproject.scalc.application.project.usecase;

import local.myproject.scalc.application.project.port.out.ProjectPort;
import local.myproject.scalc.domain.aggregate.project.Project;
import local.myproject.scalc.infrastructure.persistent.dao.project.ProjectDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса работы с проектами.
 *
 * @author Evgenii Mironov
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectLifecycleUseCase implements ProjectPort {

    public final ProjectDao projectDao;

    @Override
    /**
     * Возвращает проекты пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список проектов
     */
    public List<Project> findAllByUser(Long userId) {
        return projectDao.findAllByUserId(userId);
    }

    @Override
    /**
     * Возвращает проект по идентификатору.
     *
     * @param projectId идентификатор проекта
     * @return найденный проект
     */
    public Project findById(int projectId) {
        return projectDao.findById(projectId).orElseThrow();
    }

    @Override
    /**
     * Сохраняет проект.
     *
     * @param project проект для сохранения
     * @return результат не возвращается
     */
    public void save(Project project) {
        projectDao.save(project);
    }

    @Override
    /**
     * Обновляет проект.
     *
     * @param project проект для обновления
     * @return результат не возвращается
     */
    public void update(Project project) {
        projectDao.update(project);
    }

    @Override
    /**
     * Удаляет проект по идентификатору.
     *
     * @param projectId идентификатор проекта
     * @return результат не возвращается
     */
    public void deleteById(int projectId) {
        projectDao.deleteById(projectId);
    }
}
