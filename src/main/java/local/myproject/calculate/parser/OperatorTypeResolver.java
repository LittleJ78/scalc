package local.myproject.calculate.parser;

import local.myproject.calculate.model.Operator;
import local.myproject.calculate.model.Unit;
import local.myproject.calculate.operator.TypeOfOperators;
import java.util.ArrayList;
import java.util.List;

/**
 * Компонент переопределения типов операторов внутри выражения.
 *
 * @author Evgenii Mironov
 */
public final class OperatorTypeResolver {
    /**
     * Уточняет тип операторов с контекстно-зависимым поведением.
     *
     * @param expression список юнитов выражения
     * @return список юнитов с уточненными типами операторов
     */
    public List<Unit> resolve(List<Unit> expression) {
        List<Unit> resolvedExpression = new ArrayList<>(expression);
        if (!resolvedExpression.isEmpty()
                && Operator.class.isInstance(resolvedExpression.get(0))
                && resolvedExpression.get(0).getValue().equals("-")) {
            ((Operator) resolvedExpression.get(0)).setTypeOfOperator(TypeOfOperators.PrefixUnaryOperators);
        }
        for (int i = 1; i < resolvedExpression.size(); i++) {
            if (Operator.class.isInstance(resolvedExpression.get(i)) && resolvedExpression.get(i).getValue().equals("-")) {
                if (Operator.class.isInstance(resolvedExpression.get(i - 1))
                        && !resolvedExpression.get(i - 1).getValue().equals(")")
                        && !resolvedExpression.get(i - 1).getType().equals(TypeOfOperators.PostfixUnaryOperators)) {
                    ((Operator) resolvedExpression.get(i)).setTypeOfOperator(TypeOfOperators.PrefixUnaryOperators);
                }
            }
        }
        return resolvedExpression;
    }
}
