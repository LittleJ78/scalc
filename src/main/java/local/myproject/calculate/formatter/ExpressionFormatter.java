package local.myproject.calculate.formatter;

import local.myproject.calculate.converter.Converter;
import local.myproject.calculate.model.Operand;
import local.myproject.calculate.model.Unit;
import local.myproject.calculate.operator.TypeOfOperands;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Компонент форматирования выражений и атомарных действий.
 *
 * @author Evgenii Mironov
 */
public final class ExpressionFormatter {
    private static final String DEFAULT_NAME = "DefaultName";

    /**
     * Преобразует набор юнитов в строковое выражение.
     *
     * @param expression набор юнитов
     * @param type тип вывода операндов
     * @return строковое выражение
     */
    public String format(Collection<Unit> expression, TypeOfOperands type) {
        return expression.stream()
                .map(unit -> Operand.class.isInstance(unit)
                        ? formatOperand((Operand) unit, type)
                        : unit.getValue())
                .collect(Collectors.joining(" "));
    }

    /**
     * Преобразует атомарное выражение в строку.
     *
     * @param atomicExpression атомарное выражение
     * @param type тип вывода операндов
     * @return строковое представление атомарного выражения
     */
    public String formatAtomic(List<Unit> atomicExpression, TypeOfOperands type) {
        return format(atomicExpression, type).trim();
    }

    /**
     * Преобразует операнд в строку с учетом имени параметра и типа вывода.
     *
     * @param operand операнд
     * @param type тип вывода
     * @return строковое представление операнда
     */
    private String formatOperand(Operand operand, TypeOfOperands type) {
        if (!operand.getName().equals(DEFAULT_NAME)) {
            return operand.getName();
        }
        return type != null ? Converter.operandToString(operand, type) : Converter.operandToString(operand);
    }
}
