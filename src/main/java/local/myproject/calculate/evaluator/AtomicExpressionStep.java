package local.myproject.calculate.evaluator;

import local.myproject.calculate.model.Operand;
import local.myproject.calculate.model.Unit;

import java.util.List;

/**
 * Атомарный шаг вычисления выражения.
 *
 * @author Evgenii Mironov
 * @param expression атомарное выражение в виде набора юнитов
 * @param result результат вычисления атомарного выражения
 */
public record AtomicExpressionStep(List<Unit> expression, Operand result) {
}
