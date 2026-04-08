package local.myproject.scalc.application.expression.usecase;

import local.myproject.scalc.application.expression.command.CalculateProjectExpressionCommand;
import local.myproject.scalc.application.expression.command.CreateExpressionCommand;
import local.myproject.scalc.application.expression.command.DeleteExpressionCommand;
import local.myproject.scalc.application.expression.command.UpdateExpressionCommand;
import local.myproject.scalc.application.expression.port.in.ExpressionUseCaseIn;
import local.myproject.scalc.application.expression.port.out.ExpressionCalculatorPort;
import local.myproject.scalc.application.expression.port.out.ExpressionParameterPort;
import local.myproject.scalc.application.expression.port.out.ExpressionUnitPort;
import local.myproject.scalc.application.expression.query.FindExpressionByIdQuery;
import local.myproject.scalc.application.expression.query.FindExpressionsByProjectQuery;
import local.myproject.scalc.application.project.usecase.ProjectUseCase;
import local.myproject.scalc.application.user.port.out.UserPort;
import local.myproject.scalc.domain.aggregate.expression.ExpressionUnit;
import local.myproject.scalc.domain.aggregate.project.Project;
import local.myproject.scalc.domain.aggregate.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Use case работы с выражениями проекта.
 *
 * @author Evgenii Mironov
 */
@Service
@RequiredArgsConstructor
public class ExpressionUseCase implements ExpressionUseCaseIn {
    private final ExpressionCalculatorPort expressionCalculatorPort;
    private final ExpressionParameterPort expressionParameterPort;
    private final ExpressionUnitPort expressionUnitPort;
    private final ProjectUseCase projectUseCase;
    private final UserPort userPort;

    /**
     * Возвращает список выражений проекта.
     *
     * @param query запрос списка выражений
     * @return список выражений
     */
    public List<ExpressionUnit> findAllByProject(FindExpressionsByProjectQuery query) {
        Project project = getOwnedProject(query.getUserName(), query.getProjectId());
        String[][] parameters = expressionParameterPort.findParametersByProjectId(project.getProjectId());
        return expressionUnitPort.findAllByProjectId(project.getProjectId()).stream()
                .map(expressionUnit -> expressionCalculatorPort.calculate(expressionUnit, parameters))
                .toList();
    }

    /**
     * Выполняет расчет выражения без сохранения.
     *
     * @param command команда расчета выражения
     * @return рассчитанное выражение
     * @throws Exception если выражение не удалось обработать
     */
    public ExpressionUnit calculate(CalculateProjectExpressionCommand command) throws Exception {
        Project project = getOwnedProject(command.getUserName(), command.getProjectId());
        return expressionCalculatorPort.calculate(
                toExpressionUnit(command, project),
                expressionParameterPort.findParametersByProjectId(project.getProjectId())
        );
    }

    /**
     * Создает и сохраняет новое выражение.
     *
     * @param command команда создания выражения
     * @return созданное выражение
     * @throws Exception если выражение не удалось обработать
     */
    public ExpressionUnit create(CreateExpressionCommand command) throws Exception {
        Project project = getOwnedProject(command.getUserName(), command.getProjectId());
        ExpressionUnit expressionUnit = expressionCalculatorPort.calculate(
                toExpressionUnit(command, project),
                expressionParameterPort.findParametersByProjectId(project.getProjectId())
        );
        expressionUnitPort.save(expressionUnit);
        return expressionUnit;
    }

    /**
     * Возвращает выражение по идентификатору.
     *
     * @param query запрос на получение выражения
     * @return найденное выражение
     */
    public ExpressionUnit findById(FindExpressionByIdQuery query) {
        ExpressionUnit expressionUnit = getOwnedExpression(query.getUserName(), query.getExpressionUnitId());
        return expressionCalculatorPort.calculate(
                expressionUnit,
                expressionParameterPort.findParametersByProjectId(expressionUnit.getProject().getProjectId())
        );
    }

    /**
     * Обновляет существующее выражение.
     *
     * @param command команда обновления выражения
     * @return обновленное выражение
     * @throws Exception если выражение не удалось обработать
     */
    public ExpressionUnit update(UpdateExpressionCommand command) throws Exception {
        ExpressionUnit expressionUnit = getOwnedExpression(command.getUserName(), command.getExpressionUnitId());
        expressionUnit.setTypeOfOperand(defaultOperandType(command.getTypeOfOperand()));
        if (command.getExpressionUnitName() != null && !command.getExpressionUnitName().isBlank()) {
            expressionUnit.setExpressionUnitName(command.getExpressionUnitName());
        }
        expressionUnit.setDefaultExpression(command.getDefaultExpression() == null ? "" : command.getDefaultExpression());
        expressionUnit.setWatchList(command.isWatchList());
        expressionCalculatorPort.calculate(
                expressionUnit,
                expressionParameterPort.findParametersByProjectId(expressionUnit.getProject().getProjectId())
        );
        expressionUnitPort.update(expressionUnit);
        return expressionUnit;
    }

    /**
     * Удаляет выражение.
     *
     * @param command команда удаления выражения
     * @return результат не возвращается
     */
    public void delete(DeleteExpressionCommand command) {
        getOwnedExpression(command.getUserName(), command.getExpressionUnitId());
        expressionUnitPort.deleteById(command.getExpressionUnitId());
    }

    /**
     * Создает доменную модель выражения для расчета.
     *
     * @param command команда расчета выражения
     * @param project проект владельца выражения
     * @return доменная модель выражения
     * @throws Exception если выражение не удалось подготовить
     */
    private ExpressionUnit toExpressionUnit(CalculateProjectExpressionCommand command, Project project) throws Exception {
        ExpressionUnit expressionUnit = new ExpressionUnit(defaultOperandType(command.getTypeOfOperand()));
        if (command.getExpressionUnitName() != null && !command.getExpressionUnitName().isBlank()) {
            expressionUnit.setExpressionUnitName(command.getExpressionUnitName());
        }
        expressionUnit.setDefaultExpression(command.getDefaultExpression() == null ? "" : command.getDefaultExpression());
        expressionUnit.setWatchList(command.isWatchList());
        expressionUnit.setProject(project);
        return expressionUnit;
    }

    /**
     * Создает доменную модель выражения для сохранения.
     *
     * @param command команда создания выражения
     * @param project проект владельца выражения
     * @return доменная модель выражения
     * @throws Exception если выражение не удалось подготовить
     */
    private ExpressionUnit toExpressionUnit(CreateExpressionCommand command, Project project) throws Exception {
        ExpressionUnit expressionUnit = new ExpressionUnit(defaultOperandType(command.getTypeOfOperand()));
        if (command.getExpressionUnitName() != null && !command.getExpressionUnitName().isBlank()) {
            expressionUnit.setExpressionUnitName(command.getExpressionUnitName());
        }
        expressionUnit.setDefaultExpression(command.getDefaultExpression() == null ? "" : command.getDefaultExpression());
        expressionUnit.setWatchList(command.isWatchList());
        expressionUnit.setProject(project);
        return expressionUnit;
    }

    /**
     * Возвращает тип операнда по умолчанию.
     *
     * @param typeOfOperand тип операнда
     * @return итоговый тип операнда
     */
    private String defaultOperandType(String typeOfOperand) {
        return typeOfOperand == null || typeOfOperand.isBlank() ? "Default" : typeOfOperand;
    }

    /**
     * Возвращает текущего пользователя по имени.
     *
     * @param userName имя пользователя
     * @return пользователь
     */
    private User getCurrentUser(String userName) {
        return userPort.findByUserName(userName);
    }

    /**
     * Возвращает проект, принадлежащий пользователю.
     *
     * @param userName имя пользователя
     * @param projectId идентификатор проекта
     * @return проект пользователя
     */
    private Project getOwnedProject(String userName, int projectId) {
        return projectUseCase.getOwnedProject(userName, projectId);
    }

    /**
     * Возвращает выражение, принадлежащее пользователю.
     *
     * @param userName имя пользователя
     * @param expressionUnitId идентификатор выражения
     * @return выражение пользователя
     */
    private ExpressionUnit getOwnedExpression(String userName, int expressionUnitId) {
        User user = getCurrentUser(userName);
        ExpressionUnit expressionUnit = expressionUnitPort.findById(expressionUnitId);
        if (expressionUnit.getProject() == null
                || expressionUnit.getProject().getUser() == null
                || !expressionUnit.getProject().getUser().getUserId().equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expression not found");
        }
        return expressionUnit;
    }
}
