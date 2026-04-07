package local.myproject.scalc.persistent.dao;

import local.myproject.scalc.domain.ExpressionUnit;
import local.myproject.scalc.domain.Project;
import local.myproject.scalc.persistent.dto.ExpressionUnitDbDto;
import local.myproject.scalc.persistent.mapper.ExpressionUnitPersistentMapper;
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
public class JdbcExpressionUnitDao implements ExpressionUnitDao {
    private final JdbcTemplate jdbcTemplate;
    private final ExpressionUnitPersistentMapper expressionUnitPersistentMapper;
    private final RowMapper<ExpressionUnitDbDto> rowMapper = (rs, rowNum) -> {
        ExpressionUnitDbDto dto = new ExpressionUnitDbDto();
        dto.setExpressionUnitId(rs.getInt("expression_unit_id"));
        dto.setTypeOfOperand(rs.getString("type_of_operand"));
        dto.setExpressionUnitName(rs.getString("expression_unit_name"));
        dto.setDefaultExpression(rs.getString("default_expression"));
        dto.setExpressionResult(rs.getString("expression_result"));
        dto.setProjectId(rs.getInt("project_id"));
        dto.setWatchList(rs.getBoolean("watch_list"));
        return dto;
    };

    public JdbcExpressionUnitDao(JdbcTemplate jdbcTemplate, ExpressionUnitPersistentMapper expressionUnitPersistentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.expressionUnitPersistentMapper = expressionUnitPersistentMapper;
    }

    @Override
    public List<ExpressionUnit> findAll() {
        return jdbcTemplate.query(
                "SELECT expression_unit_id, type_of_operand, expression_unit_name, default_expression, expression_result, project_id, watch_list FROM expression_units",
                rowMapper
        ).stream().map(this::hydrateExpressionUnit).toList();
    }

    @Override
    public List<ExpressionUnit> findAllByProjectId(int projectId) {
        return jdbcTemplate.query(
                "SELECT expression_unit_id, type_of_operand, expression_unit_name, default_expression, expression_result, project_id, watch_list FROM expression_units WHERE project_id = ? ORDER BY expression_unit_id",
                rowMapper,
                projectId
        ).stream().map(this::hydrateExpressionUnit).toList();
    }

    @Override
    public List<String> findAllNameByProjectId(int projectId) {
        return jdbcTemplate.query(
                "SELECT expression_unit_name FROM expression_units WHERE project_id = ? ORDER BY expression_unit_id",
                (rs, rowNum) -> rs.getString("expression_unit_name"),
                projectId
        );
    }

    @Override
    public Optional<ExpressionUnit> findById(int expressionUnitId) {
        List<ExpressionUnitDbDto> expressionUnits = jdbcTemplate.query(
                "SELECT expression_unit_id, type_of_operand, expression_unit_name, default_expression, expression_result, project_id, watch_list FROM expression_units WHERE expression_unit_id = ?",
                rowMapper,
                expressionUnitId
        );
        return expressionUnits.stream().findFirst().map(this::hydrateExpressionUnit);
    }

    @Override
    public ExpressionUnit save(ExpressionUnit expressionUnit) {
        ExpressionUnitDbDto dto = expressionUnitPersistentMapper.toDto(expressionUnit);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO expression_units (type_of_operand, expression_unit_name, default_expression, expression_result, project_id, watch_list) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, dto.getTypeOfOperand());
            statement.setString(2, dto.getExpressionUnitName());
            statement.setString(3, dto.getDefaultExpression());
            statement.setString(4, dto.getExpressionResult());
            statement.setInt(5, dto.getProjectId());
            statement.setBoolean(6, dto.isWatchList());
            return statement;
        }, keyHolder);
        expressionUnit.setExpressionUnitId(keyHolder.getKey().intValue());
        return findById(expressionUnit.getExpressionUnitId()).orElseThrow();
    }

    @Override
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
    public void deleteById(int expressionUnitId) {
        jdbcTemplate.update("DELETE FROM expression_units WHERE expression_unit_id = ?", expressionUnitId);
    }

    private ExpressionUnit hydrateExpressionUnit(ExpressionUnitDbDto dto) {
        ExpressionUnit expressionUnit = expressionUnitPersistentMapper.toEntity(dto);
        Project project = new Project();
        project.setProjectId(dto.getProjectId());
        expressionUnit.setProject(project);
        return expressionUnit;
    }
}
