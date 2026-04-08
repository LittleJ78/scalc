package local.myproject.scalc.application.expression.port.in;

import local.myproject.scalc.application.expression.command.CalculateProjectExpressionCommand;
import local.myproject.scalc.application.expression.command.CreateExpressionCommand;
import local.myproject.scalc.application.expression.command.DeleteExpressionCommand;
import local.myproject.scalc.application.expression.command.UpdateExpressionCommand;
import local.myproject.scalc.application.expression.query.FindExpressionByIdQuery;
import local.myproject.scalc.application.expression.query.FindExpressionsByProjectQuery;
import local.myproject.scalc.domain.aggregate.expression.ExpressionUnit;

import java.util.List;

/**
 * Входной порт сценариев управления выражениями.
 *
 * @author Evgenii Mironov
 */
public interface ExpressionUseCaseIn {
    /**
     * Возвращает список выражений проекта.
     *
     * @param query запрос списка выражений
     * @return список выражений
     */
    List<ExpressionUnit> findAllByProject(FindExpressionsByProjectQuery query);

    /**
     * Выполняет расчет выражения без сохранения.
     *
     * @param command команда расчета выражения
     * @return рассчитанное выражение
     * @throws Exception если вычисление завершилось ошибкой
     */
    ExpressionUnit calculate(CalculateProjectExpressionCommand command) throws Exception;

    /**
     * Создает новое выражение.
     *
     * @param command команда создания выражения
     * @return созданное выражение
     * @throws Exception если создание завершилось ошибкой
     */
    ExpressionUnit create(CreateExpressionCommand command) throws Exception;

    /**
     * Возвращает выражение по идентификатору.
     *
     * @param query запрос получения выражения
     * @return найденное выражение
     */
    ExpressionUnit findById(FindExpressionByIdQuery query);

    /**
     * Обновляет выражение.
     *
     * @param command команда обновления выражения
     * @return обновленное выражение
     * @throws Exception если обновление завершилось ошибкой
     */
    ExpressionUnit update(UpdateExpressionCommand command) throws Exception;

    /**
     * Удаляет выражение.
     *
     * @param command команда удаления выражения
     * @return результат не возвращается
     */
    void delete(DeleteExpressionCommand command);
}
