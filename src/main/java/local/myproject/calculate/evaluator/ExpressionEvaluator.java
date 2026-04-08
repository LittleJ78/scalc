package local.myproject.calculate.evaluator;

import local.myproject.calculate.model.Operand;
import local.myproject.calculate.model.Operator;
import local.myproject.calculate.model.Unit;
import local.myproject.calculate.operator.TypeOfOperators;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Компонент вычисления выражения в польской записи.
 *
 * @author Evgenii Mironov
 */
public final class ExpressionEvaluator {
    /**
     * Вычисляет польскую запись и возвращает результат вместе с атомарными действиями.
     *
     * @param polishRecord польская запись
     * @return результат вычисления
     */
    public EvaluationResult evaluate(Deque<Unit> polishRecord) {
        Deque<Unit> stack = new LinkedList<>();
        Deque<Unit> localPolishRecord = new LinkedList<>(polishRecord);
        List<AtomicExpressionStep> atomicExpressions = new ArrayList<>();

        while (!localPolishRecord.isEmpty()) {
            if (Operand.class.isInstance(localPolishRecord.peekFirst())) {
                stack.addLast(localPolishRecord.pollFirst());
            }
            if (Operator.class.isInstance(localPolishRecord.peekFirst())) {
                Operand secondOperand = (Operand) stack.pollLast();
                Operator operator = (Operator) localPolishRecord.pollFirst();
                if (operator.getType().equals(TypeOfOperators.BinaryOperators)) {
                    Operand firstOperand = (Operand) stack.pollLast();
                    Operand result = firstOperand.apply(operator, secondOperand);
                    stack.addLast(result);
                    atomicExpressions.add(new AtomicExpressionStep(List.of(firstOperand, operator, secondOperand), result));
                    validateFiniteResult(result, firstOperand, operator, secondOperand);
                } else {
                    Operand result = secondOperand.apply(operator);
                    stack.addLast(result);
                    if (operator.getType().equals(TypeOfOperators.PrefixUnaryOperators)) {
                        atomicExpressions.add(new AtomicExpressionStep(List.of(operator, secondOperand), result));
                    } else if (operator.getType().equals(TypeOfOperators.PostfixUnaryOperators)) {
                        atomicExpressions.add(new AtomicExpressionStep(List.of(secondOperand, operator), result));
                    }
                    validateFiniteUnaryResult(result, operator, secondOperand);
                }
            }
        }

        if (stack.size() != 1) {
            throw new ArithmeticException("Ошибка вычисления выражения");
        }
        return new EvaluationResult((Operand) stack.pollFirst(), atomicExpressions);
    }

    /**
     * Проверяет результат бинарной операции на специальные значения.
     *
     * @param result итоговый результат
     * @param firstOperand первый операнд
     * @param operator оператор
     * @param secondOperand второй операнд
     * @return результат не возвращается
     */
    private void validateFiniteResult(Operand result, Operand firstOperand, Operator operator, Operand secondOperand) {
        switch (result.getValue()) {
            case "Infinity" -> throw new ArithmeticException("Infinity");
            case "NaN" -> throw new ArithmeticException("Not-a-Number");
            default -> {
            }
        }
    }

    /**
     * Проверяет результат унарной операции на специальные значения.
     *
     * @param result итоговый результат
     * @param operator оператор
     * @param operand операнд
     * @return результат не возвращается
     */
    private void validateFiniteUnaryResult(Operand result, Operator operator, Operand operand) {
        switch (result.getValue()) {
            case "Infinity" -> throw new ArithmeticException("Infinity");
            case "NaN" -> throw new ArithmeticException("Not-a-Number");
            default -> {
            }
        }
    }
}
