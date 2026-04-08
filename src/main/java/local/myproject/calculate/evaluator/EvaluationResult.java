package local.myproject.calculate.evaluator;

import local.myproject.calculate.model.Operand;
import java.util.List;

/**
 * Результат вычисления польской записи.
 *
 * @author Evgenii Mironov
 * @param operand итоговый операнд
 * @param atomicExpressions список атомарных шагов вычисления
 */
public record EvaluationResult(Operand operand, List<AtomicExpressionStep> atomicExpressions) {
}
