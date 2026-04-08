package local.myproject.scalc.infrastructure.persistent.dto.expression;

/**
 * DTO строки таблицы параметров выражений.
 *
 * @author Evgenii Mironov
 */
public record ParameterForExpressionDbDto(
        Integer parametersForExpressionsId,
        String parameter,
        Integer parametrisedExpressionId
) {
}
