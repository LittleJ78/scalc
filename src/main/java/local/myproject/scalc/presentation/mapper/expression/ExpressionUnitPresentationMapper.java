package local.myproject.scalc.presentation.mapper.expression;

import local.myproject.scalc.domain.aggregate.expression.ExpressionUnit;
import local.myproject.scalc.domain.aggregate.project.Project;
import local.myproject.scalc.presentation.dto.expression.ExpressionUnitDto;
import org.springframework.stereotype.Component;

/**
 * Маппер выражений между доменной моделью и REST DTO.
 *
 * @author Evgenii Mironov
 */
@Component
public class ExpressionUnitPresentationMapper {
    /**
     * Преобразует доменную модель выражения в DTO.
     *
     * @param model доменная модель выражения
     * @return DTO выражения
     */
    public ExpressionUnitDto toDto(ExpressionUnit model) {
        try {
            return new ExpressionUnitDto(
                    model.getExpressionUnitId(),
                    model.getTypeOfOperand(),
                    model.getExpressionUnitName(),
                    model.getDefaultExpression(),
                    model.getExpressionResult(),
                    model.getProject() == null ? null : model.getProject().getProjectId(),
                    model.isWatchList(),
                    model.getUnitExpression(),
                    model.getAtomicExpressions()
            );
        } catch (Exception exception) {
            throw new IllegalStateException("Не удалось подготовить DTO выражения", exception);
        }
    }

    /**
     * Преобразует DTO выражения в доменную модель.
     *
     * @param dto DTO выражения
     * @return доменная модель выражения
     */
    public ExpressionUnit toDomain(ExpressionUnitDto dto) {
        ExpressionUnit expressionUnit = new ExpressionUnit();
        expressionUnit.setExpressionUnitId(dto.expressionUnitId());
        expressionUnit.setTypeOfOperand(dto.typeOfOperand());
        expressionUnit.setExpressionUnitName(dto.expressionUnitName());
        expressionUnit.setExpressionResult(dto.expressionResult());
        expressionUnit.setWatchList(dto.watchList());
        if (dto.projectId() != null) {
            Project project = new Project();
            project.setProjectId(dto.projectId());
            expressionUnit.setProject(project);
        }
        try {
            expressionUnit.setDefaultExpression(dto.defaultExpression() == null ? "" : dto.defaultExpression());
        } catch (Exception exception) {
            throw new IllegalStateException("Не удалось восстановить выражение из DTO", exception);
        }
        return expressionUnit;
    }
}
