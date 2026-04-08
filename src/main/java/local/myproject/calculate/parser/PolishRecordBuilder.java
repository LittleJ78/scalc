package local.myproject.calculate.parser;

import local.myproject.calculate.model.Operand;
import local.myproject.calculate.model.Operator;
import local.myproject.calculate.model.Unit;
import local.myproject.calculate.operator.Operators;
import local.myproject.calculate.operator.TypeOfOperators;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Компонент построения польской записи.
 *
 * @author Evgenii Mironov
 */
public final class PolishRecordBuilder {
    /**
     * Строит польскую запись из списка юнитов выражения.
     *
     * @param unitExpression список юнитов выражения
     * @return польская запись
     */
    public Deque<Unit> build(List<Unit> unitExpression) {
        Deque<Unit> stack = new LinkedList<>();
        Deque<Unit> result = new LinkedList<>();

        for (Unit unit : unitExpression) {
            if (Operand.class.isInstance(unit)) {
                result.add(unit);
            }
            if (Operator.class.isInstance(unit)) {
                Operator currentOperator = (Operator) unit;
                for (Operators operator : Operators.values()) {
                    if (operator.getOperator().equals(currentOperator.getValue())) {
                        if (currentOperator.getType().equals(TypeOfOperators.BinaryOperators)) {
                            while (!stack.isEmpty()
                                    && ((stack.peekLast().getType().equals(TypeOfOperators.PrefixUnaryOperators)
                                    || stack.peekLast().getPriority() >= currentOperator.getPriority())
                                    && !stack.peekLast().getType().equals(TypeOfOperators.Brackets))) {
                                result.add(stack.pollLast());
                            }
                        }
                        if (currentOperator.getValue().equals(")")) {
                            while (!stack.peekLast().getValue().equals("(")) {
                                result.add(stack.pollLast());
                            }
                            stack.pollLast();
                        } else {
                            stack.addLast(currentOperator);
                        }
                    }
                }
            }
        }

        while (!stack.isEmpty()) {
            result.add(stack.pollLast());
        }
        return result;
    }
}
