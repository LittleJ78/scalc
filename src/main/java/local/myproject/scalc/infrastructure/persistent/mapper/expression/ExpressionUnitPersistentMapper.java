package local.myproject.scalc.infrastructure.persistent.mapper.expression;

import local.myproject.scalc.domain.aggregate.expression.ExpressionUnit;
import local.myproject.scalc.domain.aggregate.project.Project;
import local.myproject.scalc.infrastructure.persistent.dto.expression.ExpressionUnitDbDto;
import org.springframework.stereotype.Component;

/**
 * Маппер выражений между доменной моделью и persistence DTO.
 *
 * @author Evgenii Mironov
 */
@Component
public class ExpressionUnitPersistentMapper {
    /**
     * Преобразует DTO базы данных в доменную модель выражения.
     *
     * @param dto DTO строки базы данных
     * @return доменная модель выражения
     */
    public ExpressionUnit toEntity(ExpressionUnitDbDto dto) {
        ExpressionUnit expressionUnit = new ExpressionUnit();
        expressionUnit.setExpressionUnitId(dto.expressionUnitId() == null ? 0 : dto.expressionUnitId());
        expressionUnit.setTypeOfOperand(dto.typeOfOperand());
        expressionUnit.setExpressionUnitName(dto.expressionUnitName());
        expressionUnit.setExpressionResult(dto.expressionResult());
        expressionUnit.setWatchList(dto.watchList());
        Project project = new Project();
        project.setProjectId(dto.projectId() == null ? 0 : dto.projectId());
        expressionUnit.setProject(project);
        try {
            expressionUnit.setDefaultExpression(dto.defaultExpression());
        } catch (Exception exception) {
            throw new IllegalStateException("Не удалось восстановить выражение из базы данных", exception);
        }
        return expressionUnit;
    }

    /**
     * Преобразует доменную модель выражения в DTO базы данных.
     *
     * @param expressionUnit доменная модель выражения
     * @return DTO базы данных
     */
    public ExpressionUnitDbDto toDto(ExpressionUnit expressionUnit) {
        return new ExpressionUnitDbDto(
                expressionUnit.getExpressionUnitId(),
                expressionUnit.getTypeOfOperand(),
                expressionUnit.getExpressionUnitName(),
                expressionUnit.getDefaultExpression(),
                expressionUnit.getExpressionResult(),
                expressionUnit.getProject() == null ? null : expressionUnit.getProject().getProjectId(),
                expressionUnit.isWatchList()
        );
    }
}
