package local.myproject.scalc.persistent.dao;

import local.myproject.scalc.domain.ParametersForExpressions;
import local.myproject.scalc.domain.ParametrisedExpressions;
import local.myproject.scalc.persistent.dto.ParameterForExpressionDbDto;
import local.myproject.scalc.persistent.dto.ParametrisedExpressionDbDto;
import local.myproject.scalc.persistent.mapper.ParameterForExpressionPersistentMapper;
import local.myproject.scalc.persistent.mapper.ParametrisedExpressionPersistentMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class JdbcParametrisedExpressionDao implements ParametrisedExpressionDao {
    private final JdbcTemplate jdbcTemplate;
    private final ParametrisedExpressionPersistentMapper parametrisedExpressionPersistentMapper;
    private final ParameterForExpressionPersistentMapper parameterForExpressionPersistentMapper;
    private final RowMapper<ParametrisedExpressionDbDto> parametrisedExpressionRowMapper = (rs, rowNum) -> {
        ParametrisedExpressionDbDto dto = new ParametrisedExpressionDbDto();
        dto.setParametrisedExpressionId(rs.getInt("parametrised_expression_id"));
        dto.setExpressionUnitId(rs.getInt("expression_unit_id"));
        return dto;
    };
    private final RowMapper<ParameterForExpressionDbDto> parameterRowMapper = (rs, rowNum) -> {
        ParameterForExpressionDbDto dto = new ParameterForExpressionDbDto();
        dto.setParametersForExpressionsId(rs.getInt("parameters_for_expressions_id"));
        dto.setParameter(rs.getString("parameter"));
        dto.setParametrisedExpressionId(rs.getInt("parametrised_expression_id"));
        return dto;
    };

    public JdbcParametrisedExpressionDao(JdbcTemplate jdbcTemplate,
                                         ParametrisedExpressionPersistentMapper parametrisedExpressionPersistentMapper,
                                         ParameterForExpressionPersistentMapper parameterForExpressionPersistentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.parametrisedExpressionPersistentMapper = parametrisedExpressionPersistentMapper;
        this.parameterForExpressionPersistentMapper = parameterForExpressionPersistentMapper;
    }

    @Override
    public Optional<ParametrisedExpressions> findByExpressionUnitId(int expressionUnitId) {
        List<ParametrisedExpressionDbDto> items = jdbcTemplate.query(
                "SELECT parametrised_expression_id, expression_unit_id FROM parametrised_expressions WHERE expression_unit_id = ?",
                parametrisedExpressionRowMapper,
                expressionUnitId
        );
        return items.stream().findFirst().map(this::hydrateParametrisedExpression);
    }

    @Override
    public ParametrisedExpressions save(ParametrisedExpressions parametrisedExpressions) {
        ParametrisedExpressionDbDto dto = parametrisedExpressionPersistentMapper.toDto(parametrisedExpressions);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO parametrised_expressions (expression_unit_id) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, dto.getExpressionUnitId());
            return statement;
        }, keyHolder);
        parametrisedExpressions.setParametrisedExpressionId(keyHolder.getKey().intValue());
        replaceParameters(parametrisedExpressions);
        return findByExpressionUnitId(dto.getExpressionUnitId()).orElseThrow();
    }

    @Override
    public ParametrisedExpressions update(ParametrisedExpressions parametrisedExpressions) {
        jdbcTemplate.update(
                "UPDATE parametrised_expressions SET expression_unit_id = ? WHERE parametrised_expression_id = ?",
                parametrisedExpressions.getExpressionUnit().getExpressionUnitId(),
                parametrisedExpressions.getParametrisedExpressionId()
        );
        replaceParameters(parametrisedExpressions);
        return findByExpressionUnitId(parametrisedExpressions.getExpressionUnit().getExpressionUnitId()).orElseThrow();
    }

    @Override
    public void deleteById(int parametrisedExpressionId) {
        jdbcTemplate.update("DELETE FROM parametrised_expressions WHERE parametrised_expression_id = ?", parametrisedExpressionId);
    }

    private ParametrisedExpressions hydrateParametrisedExpression(ParametrisedExpressionDbDto dto) {
        ParametrisedExpressions entity = parametrisedExpressionPersistentMapper.toEntity(dto);
        entity.setParametersForExpressions(loadParameters(entity.getParametrisedExpressionId()));
        return entity;
    }

    private Set<ParametersForExpressions> loadParameters(int parametrisedExpressionId) {
        return new HashSet<>(jdbcTemplate.query(
                "SELECT parameters_for_expressions_id, parameter, parametrised_expression_id FROM parameters_for_expressions WHERE parametrised_expression_id = ? ORDER BY parameters_for_expressions_id",
                parameterRowMapper,
                parametrisedExpressionId
        ).stream().map(parameterForExpressionPersistentMapper::toEntity).toList());
    }

    private void replaceParameters(ParametrisedExpressions parametrisedExpressions) {
        jdbcTemplate.update(
                "DELETE FROM parameters_for_expressions WHERE parametrised_expression_id = ?",
                parametrisedExpressions.getParametrisedExpressionId()
        );
        if (parametrisedExpressions.getParametersForExpressions() == null) {
            return;
        }
        for (ParametersForExpressions parameter : parametrisedExpressions.getParametersForExpressions()) {
            jdbcTemplate.update(
                    "INSERT INTO parameters_for_expressions (parameter, parametrised_expression_id) VALUES (?, ?)",
                    parameter.getParameter(),
                    parametrisedExpressions.getParametrisedExpressionId()
            );
        }
    }
}
