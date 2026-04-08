package local.myproject.scalc.infrastructure.persistent.dto.expression;

/**
 * DTO строки таблицы выражений.
 *
 * @author Evgenii Mironov
 */
public record ExpressionUnitDbDto(
        Integer expressionUnitId,
        String typeOfOperand,
        String expressionUnitName,
        String defaultExpression,
        String expressionResult,
        Integer projectId,
        boolean watchList
) {
}
