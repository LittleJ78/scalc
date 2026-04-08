package local.myproject.scalc.infrastructure.persistent.dao.expression;

import local.myproject.scalc.domain.aggregate.expression.ParametersForExpressions;
import local.myproject.scalc.domain.aggregate.expression.ParametrisedExpressions;
import local.myproject.scalc.infrastructure.persistent.dto.expression.ParameterForExpressionDbDto;
import local.myproject.scalc.infrastructure.persistent.dto.expression.ParametrisedExpressionDbDto;
import local.myproject.scalc.infrastructure.persistent.mapper.expression.ParameterForExpressionPersistentMapper;
import local.myproject.scalc.infrastructure.persistent.mapper.expression.ParametrisedExpressionPersistentMapper;
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

/**
 * JDBC-реализация DAO для параметризованных выражений.
 *
 * @author Evgenii Mironov
 */
@Repository
public class JdbcParametrisedExpressionDao implements ParametrisedExpressionDao {
    private final JdbcTemplate jdbcTemplate;
    private final ParametrisedExpressionPersistentMapper parametrisedExpressionPersistentMapper;
    private final ParameterForExpressionPersistentMapper parameterForExpressionPersistentMapper;
    private final RowMapper<ParametrisedExpressionDbDto> parametrisedExpressionRowMapper = (rs, rowNum) -> new ParametrisedExpressionDbDto(
            rs.getInt("parametrised_expression_id"),
            rs.getInt("expression_unit_id")
    );
    private final RowMapper<ParameterForExpressionDbDto> parameterRowMapper = (rs, rowNum) -> new ParameterForExpressionDbDto(
            rs.getInt("parameters_for_expressions_id"),
            rs.getString("parameter"),
            rs.getInt("parametrised_expression_id")
    );

    /**
     * Создает JDBC DAO параметризованных выражений.
     *
     * @param jdbcTemplate JDBC шаблон
     * @param parametrisedExpressionPersistentMapper маппер параметризованных выражений
     * @param parameterForExpressionPersistentMapper маппер параметров выражений
     * @return результат не возвращается
     */
    public JdbcParametrisedExpressionDao(JdbcTemplate jdbcTemplate,
                                         ParametrisedExpressionPersistentMapper parametrisedExpressionPersistentMapper,
                                         ParameterForExpressionPersistentMapper parameterForExpressionPersistentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.parametrisedExpressionPersistentMapper = parametrisedExpressionPersistentMapper;
        this.parameterForExpressionPersistentMapper = parameterForExpressionPersistentMapper;
    }

    @Override
    /**
     * Ищет параметризованное выражение по идентификатору выражения.
     *
     * @param expressionUnitId идентификатор выражения
     * @return найденное параметризованное выражение или пустой результат
     */
    public Optional<ParametrisedExpressions> findByExpressionUnitId(int expressionUnitId) {
        List<ParametrisedExpressionDbDto> items = jdbcTemplate.query(
                "SELECT parametrised_expression_id, expression_unit_id FROM parametrised_expressions WHERE expression_unit_id = ?",
                parametrisedExpressionRowMapper,
                expressionUnitId
        );
        return items.stream().findFirst().map(this::hydrateParametrisedExpression);
    }

    @Override
    /**
     * Сохраняет параметризованное выражение.
     *
     * @param parametrisedExpressions параметризованное выражение
     * @return сохраненное параметризованное выражение
     */
    public ParametrisedExpressions save(ParametrisedExpressions parametrisedExpressions) {
        ParametrisedExpressionDbDto dto = parametrisedExpressionPersistentMapper.toDto(parametrisedExpressions);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO parametrised_expressions (expression_unit_id) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, dto.expressionUnitId());
            return statement;
        }, keyHolder);
        parametrisedExpressions.setParametrisedExpressionId(keyHolder.getKey().intValue());
        replaceParameters(parametrisedExpressions);
        return findByExpressionUnitId(dto.expressionUnitId()).orElseThrow();
    }

    @Override
    /**
     * Обновляет параметризованное выражение.
     *
     * @param parametrisedExpressions параметризованное выражение
     * @return обновленное параметризованное выражение
     */
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
    /**
     * Удаляет параметризованное выражение по идентификатору.
     *
     * @param parametrisedExpressionId идентификатор параметризованного выражения
     * @return результат не возвращается
     */
    public void deleteById(int parametrisedExpressionId) {
        jdbcTemplate.update("DELETE FROM parametrised_expressions WHERE parametrised_expression_id = ?", parametrisedExpressionId);
    }

    /**
     * Наполняет доменную модель параметризованного выражения связанными параметрами.
     *
     * @param dto DTO строки базы данных
     * @return доменная модель параметризованного выражения
     */
    private ParametrisedExpressions hydrateParametrisedExpression(ParametrisedExpressionDbDto dto) {
        ParametrisedExpressions entity = parametrisedExpressionPersistentMapper.toEntity(dto);
        entity.setParametersForExpressions(loadParameters(entity.getParametrisedExpressionId()));
        return entity;
    }

    /**
     * Загружает набор параметров для параметризованного выражения.
     *
     * @param parametrisedExpressionId идентификатор параметризованного выражения
     * @return набор параметров
     */
    private Set<ParametersForExpressions> loadParameters(int parametrisedExpressionId) {
        return new HashSet<>(jdbcTemplate.query(
                "SELECT parameters_for_expressions_id, parameter, parametrised_expression_id FROM parameters_for_expressions WHERE parametrised_expression_id = ? ORDER BY parameters_for_expressions_id",
                parameterRowMapper,
                parametrisedExpressionId
        ).stream().map(parameterForExpressionPersistentMapper::toEntity).toList());
    }

    /**
     * Полностью заменяет набор параметров в базе данных.
     *
     * @param parametrisedExpressions параметризованное выражение
     * @return результат не возвращается
     */
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
