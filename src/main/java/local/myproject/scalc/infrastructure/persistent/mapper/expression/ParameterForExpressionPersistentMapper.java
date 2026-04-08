package local.myproject.scalc.infrastructure.persistent.mapper.expression;

import local.myproject.scalc.domain.aggregate.expression.ParametersForExpressions;
import local.myproject.scalc.domain.aggregate.expression.ParametrisedExpressions;
import local.myproject.scalc.infrastructure.persistent.dto.expression.ParameterForExpressionDbDto;
import org.springframework.stereotype.Component;

/**
 * Маппер параметров выражений между доменной моделью и persistence DTO.
 *
 * @author Evgenii Mironov
 */
@Component
public class ParameterForExpressionPersistentMapper {
    /**
     * Преобразует DTO базы данных в доменную модель параметра.
     *
     * @param dto DTO строки базы данных
     * @return доменная модель параметра
     */
    public ParametersForExpressions toEntity(ParameterForExpressionDbDto dto) {
        ParametersForExpressions entity = new ParametersForExpressions();
        entity.setParametersForExpressionsId(dto.parametersForExpressionsId() == null ? 0 : dto.parametersForExpressionsId());
        entity.setParameter(dto.parameter());
        ParametrisedExpressions parametrisedExpressions = new ParametrisedExpressions();
        parametrisedExpressions.setParametrisedExpressionId(dto.parametrisedExpressionId() == null ? 0 : dto.parametrisedExpressionId());
        entity.setParametrisedExpressions(parametrisedExpressions);
        return entity;
    }

    /**
     * Преобразует доменную модель параметра в DTO базы данных.
     *
     * @param entity доменная модель параметра
     * @return DTO базы данных
     */
    public ParameterForExpressionDbDto toDto(ParametersForExpressions entity) {
        return new ParameterForExpressionDbDto(
                entity.getParametersForExpressionsId(),
                entity.getParameter(),
                entity.getParametrisedExpressions() == null ? null : entity.getParametrisedExpressions().getParametrisedExpressionId()
        );
    }
}
