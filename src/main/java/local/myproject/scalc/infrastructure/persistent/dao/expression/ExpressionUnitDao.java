package local.myproject.scalc.infrastructure.persistent.dao.expression;

import local.myproject.scalc.domain.aggregate.expression.ExpressionUnit;

import java.util.List;
import java.util.Optional;

/**
 * DAO для работы с выражениями в хранилище.
 *
 * @author Evgenii Mironov
 */
public interface ExpressionUnitDao {
    /**
     * Возвращает все выражения.
     *
     * @param args параметры не передаются
     * @return список всех выражений
     */
    List<ExpressionUnit> findAll();

    /**
     * Возвращает все выражения проекта.
     *
     * @param projectId идентификатор проекта
     * @return список выражений проекта
     */
    List<ExpressionUnit> findAllByProjectId(int projectId);

    /**
     * Возвращает все имена выражений проекта.
     *
     * @param projectId идентификатор проекта
     * @return список имен выражений
     */
    List<String> findAllNameByProjectId(int projectId);

    /**
     * Ищет выражение по идентификатору.
     *
     * @param expressionUnitId идентификатор выражения
     * @return найденное выражение или пустой результат
     */
    Optional<ExpressionUnit> findById(int expressionUnitId);

    /**
     * Сохраняет выражение.
     *
     * @param expressionUnit выражение для сохранения
     * @return сохраненное выражение
     */
    ExpressionUnit save(ExpressionUnit expressionUnit);

    /**
     * Обновляет выражение.
     *
     * @param expressionUnit выражение для обновления
     * @return обновленное выражение
     */
    ExpressionUnit update(ExpressionUnit expressionUnit);

    /**
     * Удаляет выражение по идентификатору.
     *
     * @param expressionUnitId идентификатор выражения
     * @return результат не возвращается
     */
    void deleteById(int expressionUnitId);
}
