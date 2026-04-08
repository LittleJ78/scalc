package local.myproject.scalc.infrastructure.persistent.dto.expression;

/**
 * DTO строки таблицы параметризованных выражений.
 *
 * @author Evgenii Mironov
 */
public record ParametrisedExpressionDbDto(
        Integer parametrisedExpressionId,
        Integer expressionUnitId
) {
}
