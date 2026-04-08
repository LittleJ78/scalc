package local.myproject.scalc.domain.aggregate.expression;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Доменная модель параметризованного выражения.
 *
 * @author Evgenii Mironov
 */
@Getter
@Setter
@NoArgsConstructor
public class ParametrisedExpressions {
    private int parametrisedExpressionId;
    private ExpressionUnit expressionUnit;
    private Set<ParametersForExpressions> parametersForExpressions = new HashSet<>();

    /**
     * Добавляет один параметр в набор параметров выражения.
     *
     * @param parameter параметр выражения
     * @return результат не возвращается
     */
    public void addParameter(ParametersForExpressions parameter) {
        parametersForExpressions.add(parameter);
    }

    /**
     * Очищает все параметры выражения.
     *
     * @param args параметры не передаются
     * @return результат не возвращается
     */
    public void clearParameter() {
        parametersForExpressions.clear();
    }

    /**
     * Добавляет набор параметров в текущее параметризованное выражение.
     *
     * @param parameter набор параметров
     * @return результат не возвращается
     */
    public void addParameters(ParametersForExpressions... parameter) {
        for (ParametersForExpressions p : parameter) {
            p.setParametrisedExpressions(this);
            parametersForExpressions.add(p);
        }
    }
}
