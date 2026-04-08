package local.myproject.scalc.infrastructure.persistent.dao.expression;

import local.myproject.scalc.domain.aggregate.expression.ParametrisedExpressions;

import java.util.Optional;

/**
 * DAO для работы с параметризованными выражениями.
 *
 * @author Evgenii Mironov
 */
public interface ParametrisedExpressionDao {
    /**
     * Ищет параметризованное выражение по идентификатору выражения.
     *
     * @param expressionUnitId идентификатор выражения
     * @return найденное параметризованное выражение или пустой результат
     */
    Optional<ParametrisedExpressions> findByExpressionUnitId(int expressionUnitId);

    /**
     * Сохраняет параметризованное выражение.
     *
     * @param parametrisedExpressions параметризованное выражение
     * @return сохраненное параметризованное выражение
     */
    ParametrisedExpressions save(ParametrisedExpressions parametrisedExpressions);

    /**
     * Обновляет параметризованное выражение.
     *
     * @param parametrisedExpressions параметризованное выражение
     * @return обновленное параметризованное выражение
     */
    ParametrisedExpressions update(ParametrisedExpressions parametrisedExpressions);

    /**
     * Удаляет параметризованное выражение по идентификатору.
     *
     * @param parametrisedExpressionId идентификатор параметризованного выражения
     * @return результат не возвращается
     */
    void deleteById(int parametrisedExpressionId);
}
