package local.myproject.scalc.persistent.dao;

import local.myproject.scalc.domain.Project;
import local.myproject.scalc.domain.User;
import local.myproject.scalc.persistent.dto.ProjectDbDto;
import local.myproject.scalc.persistent.mapper.ProjectPersistentMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcProjectDao implements ProjectDao {
    private final JdbcTemplate jdbcTemplate;
    private final ProjectPersistentMapper projectPersistentMapper;
    private final RowMapper<ProjectDbDto> rowMapper = (rs, rowNum) -> {
        ProjectDbDto dto = new ProjectDbDto();
        dto.setProjectId(rs.getInt("project_id"));
        dto.setName(rs.getString("name"));
        dto.setDescription(rs.getString("description"));
        dto.setUserId(rs.getLong("user_id"));
        return dto;
    };

    public JdbcProjectDao(JdbcTemplate jdbcTemplate, ProjectPersistentMapper projectPersistentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.projectPersistentMapper = projectPersistentMapper;
    }

    @Override
    public List<Project> findAllByUserId(Long userId) {
        return jdbcTemplate.query(
                "SELECT project_id, name, description, user_id FROM projects WHERE user_id = ? ORDER BY project_id",
                rowMapper,
                userId
        ).stream().map(this::hydrateProject).toList();
    }

    @Override
    public Optional<Project> findById(int projectId) {
        List<ProjectDbDto> projects = jdbcTemplate.query(
                "SELECT project_id, name, description, user_id FROM projects WHERE project_id = ?",
                rowMapper,
                projectId
        );
        return projects.stream().findFirst().map(this::hydrateProject);
    }

    @Override
    public Project save(Project project) {
        ProjectDbDto dto = projectPersistentMapper.toDto(project);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO projects (name, description, user_id) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, dto.getName());
            statement.setString(2, dto.getDescription());
            statement.setLong(3, dto.getUserId());
            return statement;
        }, keyHolder);
        project.setProjectId(keyHolder.getKey().intValue());
        return findById(project.getProjectId()).orElseThrow();
    }

    @Override
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
    public void deleteById(int projectId) {
        jdbcTemplate.update("DELETE FROM projects WHERE project_id = ?", projectId);
    }

    private Project hydrateProject(ProjectDbDto dto) {
        Project project = projectPersistentMapper.toEntity(dto);
        User user = new User();
        user.setUserId(dto.getUserId());
        project.setUser(user);
        return project;
    }
}
