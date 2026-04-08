package local.myproject.calculate.parser;

import local.myproject.calculate.operator.Operators;
/**
 * Компонент нормализации алгебраических выражений.
 *
 * @author Evgenii Mironov
 */
public final class ExpressionNormalizer {
    /**
     * Нормализует выражение, расставляя пробелы вокруг операторов.
     *
     * @param expression исходное выражение
     * @return нормализованное выражение
     */
    public String normalize(String expression) {
        String normalizedExpression = expression.replaceAll(" ", "");
        for (Operators operator : Operators.values()) {
            normalizedExpression = normalizedExpression.replace(operator.getOperator(), " " + operator.getOperator() + " ");
        }
        return normalizedExpression.replaceAll("  ", " ").trim();
    }
}
