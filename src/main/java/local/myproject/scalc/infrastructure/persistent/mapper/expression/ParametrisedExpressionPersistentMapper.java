package local.myproject.scalc.infrastructure.persistent.mapper.expression;

import local.myproject.scalc.domain.aggregate.expression.ExpressionUnit;
import local.myproject.scalc.domain.aggregate.expression.ParametrisedExpressions;
import local.myproject.scalc.infrastructure.persistent.dto.expression.ParametrisedExpressionDbDto;
import org.springframework.stereotype.Component;

/**
 * Маппер параметризованных выражений между доменной моделью и persistence DTO.
 *
 * @author Evgenii Mironov
 */
@Component
public class ParametrisedExpressionPersistentMapper {
    /**
     * Преобразует DTO базы данных в доменную модель параметризованного выражения.
     *
     * @param dto DTO строки базы данных
     * @return доменная модель параметризованного выражения
     */
    public ParametrisedExpressions toEntity(ParametrisedExpressionDbDto dto) {
        ParametrisedExpressions entity = new ParametrisedExpressions();
        entity.setParametrisedExpressionId(dto.parametrisedExpressionId() == null ? 0 : dto.parametrisedExpressionId());
        ExpressionUnit expressionUnit = new ExpressionUnit();
        expressionUnit.setExpressionUnitId(dto.expressionUnitId() == null ? 0 : dto.expressionUnitId());
        entity.setExpressionUnit(expressionUnit);
        return entity;
    }

    /**
     * Преобразует доменную модель параметризованного выражения в DTO базы данных.
     *
     * @param entity доменная модель параметризованного выражения
     * @return DTO базы данных
     */
    public ParametrisedExpressionDbDto toDto(ParametrisedExpressions entity) {
        return new ParametrisedExpressionDbDto(
                entity.getParametrisedExpressionId(),
                entity.getExpressionUnit() == null ? null : entity.getExpressionUnit().getExpressionUnitId()
        );
    }
}
