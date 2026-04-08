package local.myproject.scalc.infrastructure.persistent.dao.user;

import local.myproject.scalc.domain.aggregate.project.Project;
import local.myproject.scalc.domain.aggregate.user.Role;
import local.myproject.scalc.domain.aggregate.user.User;
import local.myproject.scalc.infrastructure.persistent.dto.user.UserDbDto;
import local.myproject.scalc.infrastructure.persistent.mapper.user.UserPersistentMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * JDBC-реализация DAO для работы с пользователями.
 *
 * @author Evgenii Mironov
 */
@Repository
public class JdbcUserDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserPersistentMapper userPersistentMapper;
    private final RowMapper<UserDbDto> userRowMapper = (rs, rowNum) -> new UserDbDto(
            rs.getLong("user_id"),
            rs.getString("user_name"),
            rs.getString("password"),
            rs.getString("email")
    );

    /**
     * Создает JDBC DAO пользователей.
     *
     * @param jdbcTemplate JDBC шаблон
     * @param userPersistentMapper маппер пользователей
     * @return результат не возвращается
     */
    public JdbcUserDao(JdbcTemplate jdbcTemplate, UserPersistentMapper userPersistentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userPersistentMapper = userPersistentMapper;
    }

    @Override
    /**
     * Ищет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь или пустой результат
     */
    public Optional<User> findById(long id) {
        List<UserDbDto> users = jdbcTemplate.query(
                "SELECT user_id, user_name, password, email FROM users WHERE user_id = ?",
                userRowMapper,
                id
        );
        return users.stream().findFirst().map(this::hydrateUser);
    }

    @Override
    /**
     * Ищет пользователя по имени.
     *
     * @param userName имя пользователя
     * @return найденный пользователь или пустой результат
     */
    public Optional<User> findByUserName(String userName) {
        List<UserDbDto> users = jdbcTemplate.query(
                "SELECT user_id, user_name, password, email FROM users WHERE user_name = ?",
                userRowMapper,
                userName
        );
        return users.stream().findFirst().map(this::hydrateUser);
    }

    @Override
    /**
     * Сохраняет пользователя в базе данных.
     *
     * @param user пользователь для сохранения
     * @return сохраненный пользователь
     */
    public User save(User user) {
        UserDbDto dto = userPersistentMapper.toDto(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (user_name, password, email) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, dto.userName());
            statement.setString(2, dto.password());
            statement.setString(3, dto.email());
            return statement;
        }, keyHolder);
        user.setUserId(keyHolder.getKey().longValue());
        replaceRoles(user);
        return findById(user.getUserId()).orElseThrow();
    }

    @Override
    /**
     * Обновляет пользователя в базе данных.
     *
     * @param user пользователь для обновления
     * @return обновленный пользователь
     */
    public User update(User user) {
        jdbcTemplate.update(
                "UPDATE users SET user_name = ?, password = ?, email = ? WHERE user_id = ?",
                user.getUserName(),
                user.getPassword(),
                user.getEmail(),
                user.getUserId()
        );
        replaceRoles(user);
        return findById(user.getUserId()).orElseThrow();
    }

    @Override
    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return результат не возвращается
     */
    public void deleteById(long id) {
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
    }

    /**
     * Наполняет доменную модель пользователя ролями и проектами.
     *
     * @param dto DTO строки базы данных
     * @return доменная модель пользователя
     */
    private User hydrateUser(UserDbDto dto) {
        User user = userPersistentMapper.toEntity(dto);
        user.setRoles(loadRoles(user.getUserId()));
        user.setProjects(loadProjects(user.getUserId()));
        return user;
    }

    /**
     * Загружает роли пользователя.
     *
     * @param userId идентификатор пользователя
     * @return набор ролей пользователя
     */
    private Set<Role> loadRoles(Long userId) {
        List<String> roles = jdbcTemplate.query(
                "SELECT role FROM user_role WHERE user_id = ?",
                (rs, rowNum) -> rs.getString("role"),
                userId
        );
        Set<Role> result = new HashSet<>();
        for (String role : roles) {
            result.add(Role.valueOf(role));
        }
        return result;
    }

    /**
     * Загружает проекты пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список проектов пользователя
     */
    private List<Project> loadProjects(Long userId) {
        return new ArrayList<>(jdbcTemplate.query(
                "SELECT project_id, name, description FROM projects WHERE user_id = ?",
                (rs, rowNum) -> {
                    Project project = new Project();
                    project.setProjectId(rs.getInt("project_id"));
                    project.setName(rs.getString("name"));
                    project.setDescription(rs.getString("description"));
                    return project;
                },
                userId
        ));
    }

    /**
     * Полностью заменяет роли пользователя в базе данных.
     *
     * @param user пользователь
     * @return результат не возвращается
     */
    private void replaceRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_role WHERE user_id = ?", user.getUserId());
        if (user.getRoles() == null) {
            return;
        }
        for (Role role : user.getRoles()) {
            jdbcTemplate.update(
                    "INSERT INTO user_role (user_id, role) VALUES (?, ?)",
                    user.getUserId(),
                    role.name()
            );
        }
    }
}
