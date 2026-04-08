package local.myproject.scalc.infrastructure.persistent.dao.expression;

import local.myproject.scalc.domain.aggregate.expression.ExpressionUnit;
import local.myproject.scalc.domain.aggregate.project.Project;
import local.myproject.scalc.infrastructure.persistent.dto.expression.ExpressionUnitDbDto;
import local.myproject.scalc.infrastructure.persistent.mapper.expression.ExpressionUnitPersistentMapper;
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
 * JDBC-реализация DAO для работы с выражениями.
 *
 * @author Evgenii Mironov
 */
@Repository
public class JdbcExpressionUnitDao implements ExpressionUnitDao {
    private final JdbcTemplate jdbcTemplate;
    private final ExpressionUnitPersistentMapper expressionUnitPersistentMapper;
    private final RowMapper<ExpressionUnitDbDto> rowMapper = (rs, rowNum) -> new ExpressionUnitDbDto(
            rs.getInt("expression_unit_id"),
            rs.getString("type_of_operand"),
            rs.getString("expression_unit_name"),
            rs.getString("default_expression"),
            rs.getString("expression_result"),
            rs.getInt("project_id"),
            rs.getBoolean("watch_list")
    );

    /**
     * Создает JDBC DAO выражений.
     *
     * @param jdbcTemplate JDBC шаблон
     * @param expressionUnitPersistentMapper маппер выражений
     * @return результат не возвращается
     */
    public JdbcExpressionUnitDao(JdbcTemplate jdbcTemplate, ExpressionUnitPersistentMapper expressionUnitPersistentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.expressionUnitPersistentMapper = expressionUnitPersistentMapper;
    }

    @Override
    /**
     * Возвращает все выражения из базы данных.
     *
     * @return список выражений
     */
    public List<ExpressionUnit> findAll() {
        return jdbcTemplate.query(
                "SELECT expression_unit_id, type_of_operand, expression_unit_name, default_expression, expression_result, project_id, watch_list FROM expression_units",
                rowMapper
        ).stream().map(this::hydrateExpressionUnit).toList();
    }

    @Override
    /**
     * Возвращает все выражения проекта.
     *
     * @param projectId идентификатор проекта
     * @return список выражений проекта
     */
    public List<ExpressionUnit> findAllByProjectId(int projectId) {
        return jdbcTemplate.query(
                "SELECT expression_unit_id, type_of_operand, expression_unit_name, default_expression, expression_result, project_id, watch_list FROM expression_units WHERE project_id = ? ORDER BY expression_unit_id",
                rowMapper,
                projectId
        ).stream().map(this::hydrateExpressionUnit).toList();
    }

    @Override
    /**
     * Возвращает все имена выражений проекта.
     *
     * @param projectId идентификатор проекта
     * @return список имен выражений
     */
    public List<String> findAllNameByProjectId(int projectId) {
        return jdbcTemplate.query(
                "SELECT expression_unit_name FROM expression_units WHERE project_id = ? ORDER BY expression_unit_id",
                (rs, rowNum) -> rs.getString("expression_unit_name"),
                projectId
        );
    }

    @Override
    /**
     * Ищет выражение по идентификатору.
     *
     * @param expressionUnitId идентификатор выражения
     * @return найденное выражение или пустой результат
     */
    public Optional<ExpressionUnit> findById(int expressionUnitId) {
        List<ExpressionUnitDbDto> expressionUnits = jdbcTemplate.query(
                "SELECT expression_unit_id, type_of_operand, expression_unit_name, default_expression, expression_result, project_id, watch_list FROM expression_units WHERE expression_unit_id = ?",
                rowMapper,
                expressionUnitId
        );
        return expressionUnits.stream().findFirst().map(this::hydrateExpressionUnit);
    }

    @Override
    /**
     * Сохраняет выражение в базе данных.
     *
     * @param expressionUnit выражение для сохранения
     * @return сохраненное выражение
     */
    public ExpressionUnit save(ExpressionUnit expressionUnit) {
        ExpressionUnitDbDto dto = expressionUnitPersistentMapper.toDto(expressionUnit);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO expression_units (type_of_operand, expression_unit_name, default_expression, expression_result, project_id, watch_list) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, dto.typeOfOperand());
            statement.setString(2, dto.expressionUnitName());
            statement.setString(3, dto.defaultExpression());
            statement.setString(4, dto.expressionResult());
            statement.setInt(5, dto.projectId());
            statement.setBoolean(6, dto.watchList());
            return statement;
        }, keyHolder);
        expressionUnit.setExpressionUnitId(keyHolder.getKey().intValue());
        return findById(expressionUnit.getExpressionUnitId()).orElseThrow();
    }

    @Override
    /**
     * Обновляет выражение в базе данных.
     *
     * @param expressionUnit выражение для обновления
     * @return обновленное выражение
     */
    public ExpressionUnit update(ExpressionUnit expressionUnit) {
        jdbcTemplate.update(
                "UPDATE expression_units SET type_of_operand = ?, expression_unit_name = ?, default_expression = ?, expression_result = ?, project_id = ?, watch_list = ? WHERE expression_unit_id = ?",
                expressionUnit.getTypeOfOperand(),
                expressionUnit.getExpressionUnitName(),
                expressionUnit.getDefaultExpression(),
                expressionUnit.getExpressionResult(),
                expressionUnit.getProject().getProjectId(),
                expressionUnit.isWatchList(),
                expressionUnit.getExpressionUnitId()
        );
        return findById(expressionUnit.getExpressionUnitId()).orElseThrow();
    }

    @Override
    /**
     * Удаляет выражение по идентификатору.
     *
     * @param expressionUnitId идентификатор выражения
     * @return результат не возвращается
     */
    public void deleteById(int expressionUnitId) {
        jdbcTemplate.update("DELETE FROM expression_units WHERE expression_unit_id = ?", expressionUnitId);
    }

    /**
     * Наполняет доменную модель выражения связанными данными.
     *
     * @param dto DTO строки базы данных
     * @return доменная модель выражения
     */
    private ExpressionUnit hydrateExpressionUnit(ExpressionUnitDbDto dto) {
        ExpressionUnit expressionUnit = expressionUnitPersistentMapper.toEntity(dto);
        Project project = new Project();
        project.setProjectId(dto.projectId());
        expressionUnit.setProject(project);
        return expressionUnit;
    }
}
