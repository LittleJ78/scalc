package local.myproject.scalc.infrastructure.persistent.dao.project;

import local.myproject.scalc.domain.aggregate.project.Project;
import local.myproject.scalc.domain.aggregate.user.User;
import local.myproject.scalc.infrastructure.persistent.dto.project.ProjectDbDto;
import local.myproject.scalc.infrastructure.persistent.mapper.project.ProjectPersistentMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * JDBC-реализация DAO для работы с проектами.
 *
 * @author Evgenii Mironov
 */
@Repository
public class JdbcProjectDao implements ProjectDao {
    private final JdbcTemplate jdbcTemplate;
    private final ProjectPersistentMapper projectPersistentMapper;
    private final RowMapper<ProjectDbDto> rowMapper = (rs, rowNum) -> new ProjectDbDto(
            rs.getInt("project_id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getLong("user_id")
    );

    /**
     * Создает JDBC DAO проектов.
     *
     * @param jdbcTemplate JDBC шаблон
     * @param projectPersistentMapper маппер проектов
     * @return результат не возвращается
     */
    public JdbcProjectDao(JdbcTemplate jdbcTemplate, ProjectPersistentMapper projectPersistentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.projectPersistentMapper = projectPersistentMapper;
    }

    @Override
    /**
     * Возвращает проекты пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список проектов
     */
    public List<Project> findAllByUserId(Long userId) {
        return jdbcTemplate.query(
                "SELECT project_id, name, description, user_id FROM projects WHERE user_id = ? ORDER BY project_id",
                rowMapper,
                userId
        ).stream().map(this::hydrateProject).toList();
    }

    @Override
    /**
     * Ищет проект по идентификатору.
     *
     * @param projectId идентификатор проекта
     * @return найденный проект или пустой результат
     */
    public Optional<Project> findById(int projectId) {
        List<ProjectDbDto> projects = jdbcTemplate.query(
                "SELECT project_id, name, description, user_id FROM projects WHERE project_id = ?",
                rowMapper,
                projectId
        );
        return projects.stream().findFirst().map(this::hydrateProject);
    }

    @Override
    /**
     * Сохраняет проект в базе данных.
     *
     * @param project проект для сохранения
     * @return сохраненный проект
     */
    public Project save(Project project) {
        ProjectDbDto dto = projectPersistentMapper.toDto(project);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO projects (name, description, user_id) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, dto.name());
            statement.setString(2, dto.description());
            statement.setLong(3, dto.userId());
            return statement;
        }, keyHolder);
        project.setProjectId(keyHolder.getKey().intValue());
        return findById(project.getProjectId()).orElseThrow();
    }

    @Override
    /**
     * Обновляет проект в базе данных.
     *
     * @param project проект для обновления
     * @return обновленный проект
     */
    public Project update(Project project) {
        jdbcTemplate.update(
                "UPDATE projects SET name = ?, description = ?, user_id = ? WHERE project_id = ?",
                project.getName(),
                project.getDescription(),
                project.getUser().getUserId(),
                project.getProjectId()
        );
        return findById(project.getProjectId()).orElseThrow();
    }

    @Override
    /**
     * Удаляет проект по идентификатору.
     *
     * @param projectId идентификатор проекта
     * @return результат не возвращается
     */
    public void deleteById(int projectId) {
        jdbcTemplate.update("DELETE FROM projects WHERE project_id = ?", projectId);
    }

    /**
     * Наполняет доменную модель проекта связанным пользователем.
     *
     * @param dto DTO строки базы данных
     * @return доменная модель проекта
     */
    private Project hydrateProject(ProjectDbDto dto) {
        Project project = projectPersistentMapper.toEntity(dto);
        User user = new User();
        user.setUserId(dto.userId());
        project.setUser(user);
        return project;
    }
}
