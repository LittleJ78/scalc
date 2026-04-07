package local.myproject.scalc.persistent.mapper;

import local.myproject.scalc.domain.ExpressionUnit;
import local.myproject.scalc.domain.ParametrisedExpressions;
import local.myproject.scalc.persistent.dto.ParametrisedExpressionDbDto;
import org.springframework.stereotype.Component;

@Component
public class ParametrisedExpressionPersistentMapper {
    public ParametrisedExpressions toEntity(ParametrisedExpressionDbDto dto) {
        ParametrisedExpressions entity = new ParametrisedExpressions();
        entity.setParametrisedExpressionId(dto.getParametrisedExpressionId() == null ? 0 : dto.getParametrisedExpressionId());
        ExpressionUnit expressionUnit = new ExpressionUnit();
        expressionUnit.setExpressionUnitId(dto.getExpressionUnitId() == null ? 0 : dto.getExpressionUnitId());
        entity.setExpressionUnit(expressionUnit);
        return entity;
    }

    public ParametrisedExpressionDbDto toDto(ParametrisedExpressions entity) {
        ParametrisedExpressionDbDto dto = new ParametrisedExpressionDbDto();
        dto.setParametrisedExpressionId(entity.getParametrisedExpressionId());
        dto.setExpressionUnitId(entity.getExpressionUnit() == null ? null : entity.getExpressionUnit().getExpressionUnitId());
        return dto;
    }
}
