package local.myproject.calculate.parser;

import local.myproject.calculate.converter.Converter;
import local.myproject.calculate.model.Operand;
import local.myproject.calculate.model.Operator;
import local.myproject.calculate.model.Unit;
import local.myproject.calculate.operator.Operators;
import local.myproject.calculate.operator.TypeOfOperators;
import local.myproject.calculate.validation.Validator;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Компонент преобразования строки выражения в список юнитов.
 *
 * @author Evgenii Mironov
 */
public final class UnitExpressionParser {
    private final ExpressionNormalizer expressionNormalizer = new ExpressionNormalizer();

    /**
     * Преобразует строковое выражение в список операторов и операндов.
     *
     * @param expression исходное выражение
     * @param parameters набор параметров выражения
     * @return список юнитов выражения
     */
    public List<Unit> parse(String expression, String[][] parameters) {
        String normalizedExpression = expressionNormalizer.normalize(expression);
        List<String> stringExpression = Arrays.stream(normalizedExpression.split(" ")).toList();

        Deque<Unit> buffer = new LinkedList<>();
        for (String token : stringExpression) {
            if (Validator.validateNumber(token)) {
                buffer.add(new Operand(Validator.setTypeOfOperand(token), Converter.anyTypeNumbToDouble(token), null));
            }
            if (Validator.validateOperation(token)) {
                for (Operators operator : Operators.values()) {
                    if (operator.getOperator().equals(token)) {
                        buffer.add(new Operator(operator));
                    }
                }
            }
            appendParameter(buffer, token, parameters);
        }

        return insertImplicitMultiplication(buffer);
    }

    /**
     * Добавляет параметризованный операнд, если токен совпадает с именем параметра.
     *
     * @param buffer буфер выражения
     * @param token текущий токен
     * @param parameters набор параметров
     * @return результат не возвращается
     */
    private void appendParameter(Deque<Unit> buffer, String token, String[][] parameters) {
        if (parameters == null) {
            return;
        }
        for (String[] parameter : parameters) {
            if (parameter[0].equals(token) && Validator.validateNumber(parameter[1])) {
                buffer.add(new Operand(Validator.setTypeOfOperand(parameter[1]), Converter.anyTypeNumbToDouble(parameter[1]), parameter[0]));
            }
        }
    }

    /**
     * Вставляет неявное умножение между совместимыми соседними элементами.
     *
     * @param buffer исходный буфер
     * @return итоговый список юнитов
     */
    private List<Unit> insertImplicitMultiplication(Deque<Unit> buffer) {
        Deque<Unit> result = new LinkedList<>();
        while (!buffer.isEmpty()) {
            if (Operator.class.isInstance(buffer.peekLast()) && !result.isEmpty()) {
                if (buffer.peekLast().getValue().equals(")")
                        && (Operand.class.isInstance(result.peekFirst())
                        || result.peekFirst().getValue().equals("(")
                        || result.peekFirst().getType().equals(TypeOfOperators.PrefixUnaryOperators))) {
                    result.addFirst(new Operator(Operators.Multiplication));
                }
            }
            if (!result.isEmpty() && Operator.class.isInstance(result.peekFirst())) {
                if (result.peekFirst().getValue().equals("(") && Operand.class.isInstance(buffer.peekLast())) {
                    result.addFirst(new Operator(Operators.Multiplication));
                }
            }
            result.addFirst(buffer.pollLast());
        }
        return List.copyOf(result);
    }
}
