package local.myproject.scalc.persistent.dao;

import local.myproject.scalc.domain.Project;
import local.myproject.scalc.domain.Role;
import local.myproject.scalc.domain.User;
import local.myproject.scalc.persistent.dto.UserDbDto;
import local.myproject.scalc.persistent.mapper.UserPersistentMapper;
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

@Repository
public class JdbcUserDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserPersistentMapper userPersistentMapper;
    private final RowMapper<UserDbDto> userRowMapper = (rs, rowNum) -> {
        UserDbDto dto = new UserDbDto();
        dto.setUserId(rs.getLong("user_id"));
        dto.setUserName(rs.getString("user_name"));
        dto.setPassword(rs.getString("password"));
        dto.setEmail(rs.getString("email"));
        return dto;
    };

    public JdbcUserDao(JdbcTemplate jdbcTemplate, UserPersistentMapper userPersistentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userPersistentMapper = userPersistentMapper;
    }

    @Override
    public Optional<User> findById(long id) {
        List<UserDbDto> users = jdbcTemplate.query(
                "SELECT user_id, user_name, password, email FROM users WHERE user_id = ?",
                userRowMapper,
                id
        );
        return users.stream().findFirst().map(this::hydrateUser);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        List<UserDbDto> users = jdbcTemplate.query(
                "SELECT user_id, user_name, password, email FROM users WHERE user_name = ?",
                userRowMapper,
                userName
        );
        return users.stream().findFirst().map(this::hydrateUser);
    }

    @Override
    public User save(User user) {
        UserDbDto dto = userPersistentMapper.toDto(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (user_name, password, email) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, dto.getUserName());
            statement.setString(2, dto.getPassword());
            statement.setString(3, dto.getEmail());
            return statement;
        }, keyHolder);
        user.setUserId(keyHolder.getKey().longValue());
        replaceRoles(user);
        return findById(user.getUserId()).orElseThrow();
    }

    @Override
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
    public void deleteById(long id) {
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
    }

    private User hydrateUser(UserDbDto dto) {
        User user = userPersistentMapper.toEntity(dto);
        user.setRoles(loadRoles(user.getUserId()));
        user.setProjects(loadProjects(user.getUserId()));
        return user;
    }

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
