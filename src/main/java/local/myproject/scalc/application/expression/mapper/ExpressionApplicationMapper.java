package local.myproject.scalc.application.expression.mapper;

import local.myproject.scalc.application.expression.command.CalculateProjectExpressionCommand;
import local.myproject.scalc.application.expression.command.CreateExpressionCommand;
import local.myproject.scalc.application.expression.command.DeleteExpressionCommand;
import local.myproject.scalc.application.expression.command.EvaluateExpressionCommand;
import local.myproject.scalc.application.expression.command.UpdateExpressionCommand;
import local.myproject.scalc.application.expression.query.FindExpressionByIdQuery;
import local.myproject.scalc.application.expression.query.FindExpressionsByProjectQuery;
import local.myproject.scalc.presentation.dto.expression.ExpressionCalculationRequestDto;
import local.myproject.scalc.presentation.dto.expression.ExpressionUnitDto;
import org.springframework.stereotype.Component;

/**
 * Маппер DTO выражений в команды и запросы application-слоя.
 *
 * @author Evgenii Mironov
 */
@Component
public class ExpressionApplicationMapper {
    /**
     * Преобразует данные пользователя и проекта в запрос списка выражений.
     *
     * @param userName имя пользователя
     * @param projectId идентификатор проекта
     * @return запрос на получение списка выражений
     */
    public FindExpressionsByProjectQuery toFindAllByProjectQuery(String userName, int projectId) {
        FindExpressionsByProjectQuery query = new FindExpressionsByProjectQuery();
        query.setUserName(userName);
        query.setProjectId(projectId);
        return query;
    }

    /**
     * Преобразует DTO выражения в команду расчета внутри проекта.
     *
     * @param userName имя пользователя
     * @param projectId идентификатор проекта
     * @param request DTO выражения
     * @return команда расчета выражения
     */
    public CalculateProjectExpressionCommand toCalculateCommand(String userName, int projectId, ExpressionUnitDto request) {
        CalculateProjectExpressionCommand command = new CalculateProjectExpressionCommand();
        fillExpression(command, userName, projectId, request);
        return command;
    }

    /**
     * Преобразует DTO выражения в команду создания.
     *
     * @param userName имя пользователя
     * @param projectId идентификатор проекта
     * @param request DTO выражения
     * @return команда создания выражения
     */
    public CreateExpressionCommand toCreateCommand(String userName, int projectId, ExpressionUnitDto request) {
        CreateExpressionCommand command = new CreateExpressionCommand();
        fillExpression(command, userName, projectId, request);
        return command;
    }

    /**
     * Преобразует данные пользователя и идентификатор выражения в запрос поиска.
     *
     * @param userName имя пользователя
     * @param expressionUnitId идентификатор выражения
     * @return запрос на получение выражения
     */
    public FindExpressionByIdQuery toFindByIdQuery(String userName, int expressionUnitId) {
        FindExpressionByIdQuery query = new FindExpressionByIdQuery();
        query.setUserName(userName);
        query.setExpressionUnitId(expressionUnitId);
        return query;
    }

    /**
     * Преобразует DTO выражения в команду обновления.
     *
     * @param userName имя пользователя
     * @param expressionUnitId идентификатор выражения
     * @param request DTO выражения
     * @return команда обновления выражения
     */
    public UpdateExpressionCommand toUpdateCommand(String userName, int expressionUnitId, ExpressionUnitDto request) {
        UpdateExpressionCommand command = new UpdateExpressionCommand();
        command.setUserName(userName);
        command.setExpressionUnitId(expressionUnitId);
        command.setTypeOfOperand(request.typeOfOperand());
        command.setExpressionUnitName(request.expressionUnitName());
        command.setDefaultExpression(request.defaultExpression());
        command.setWatchList(request.watchList());
        return command;
    }

    /**
     * Преобразует параметры удаления в команду.
     *
     * @param userName имя пользователя
     * @param expressionUnitId идентификатор выражения
     * @return команда удаления выражения
     */
    public DeleteExpressionCommand toDeleteCommand(String userName, int expressionUnitId) {
        DeleteExpressionCommand command = new DeleteExpressionCommand();
        command.setUserName(userName);
        command.setExpressionUnitId(expressionUnitId);
        return command;
    }

    /**
     * Преобразует DTO разового расчета в команду вычисления.
     *
     * @param request DTO запроса на вычисление
     * @return команда разового вычисления
     */
    public EvaluateExpressionCommand toEvaluateCommand(ExpressionCalculationRequestDto request) {
        EvaluateExpressionCommand command = new EvaluateExpressionCommand();
        command.setExpression(request.expression());
        command.setTypeOfOperand(request.typeOfOperand());
        return command;
    }

    /**
     * Заполняет общие поля команд работы с выражением.
     *
     * @param target целевая команда
     * @param userName имя пользователя
     * @param projectId идентификатор проекта
     * @param request DTO выражения
     * @return результат не возвращается
     */
    private void fillExpression(Object target, String userName, int projectId, ExpressionUnitDto request) {
        if (target instanceof CalculateProjectExpressionCommand command) {
            command.setUserName(userName);
            command.setProjectId(projectId);
            command.setTypeOfOperand(request.typeOfOperand());
            command.setExpressionUnitName(request.expressionUnitName());
            command.setDefaultExpression(request.defaultExpression());
            command.setWatchList(request.watchList());
        }
        if (target instanceof CreateExpressionCommand command) {
            command.setUserName(userName);
            command.setProjectId(projectId);
            command.setTypeOfOperand(request.typeOfOperand());
            command.setExpressionUnitName(request.expressionUnitName());
            command.setDefaultExpression(request.defaultExpression());
            command.setWatchList(request.watchList());
        }
    }
}
