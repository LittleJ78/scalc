package local.myproject.scalc.persistent.mapper;

import local.myproject.scalc.domain.ParametersForExpressions;
import local.myproject.scalc.domain.ParametrisedExpressions;
import local.myproject.scalc.persistent.dto.ParameterForExpressionDbDto;
import org.springframework.stereotype.Component;

@Component
public class ParameterForExpressionPersistentMapper {
    public ParametersForExpressions toEntity(ParameterForExpressionDbDto dto) {
        ParametersForExpressions entity = new ParametersForExpressions();
        entity.setParametersForExpressionsId(dto.getParametersForExpressionsId() == null ? 0 : dto.getParametersForExpressionsId());
        entity.setParameter(dto.getParameter());
        ParametrisedExpressions parametrisedExpressions = new ParametrisedExpressions();
        parametrisedExpressions.setParametrisedExpressionId(dto.getParametrisedExpressionId() == null ? 0 : dto.getParametrisedExpressionId());
        entity.setParametrisedExpressions(parametrisedExpressions);
        return entity;
    }

    public ParameterForExpressionDbDto toDto(ParametersForExpressions entity) {
        ParameterForExpressionDbDto dto = new ParameterForExpressionDbDto();
        dto.setParametersForExpressionsId(entity.getParametersForExpressionsId());
        dto.setParameter(entity.getParameter());
        dto.setParametrisedExpressionId(entity.getParametrisedExpressions() == null ? null : entity.getParametrisedExpressions().getParametrisedExpressionId());
        return dto;
    }
}
