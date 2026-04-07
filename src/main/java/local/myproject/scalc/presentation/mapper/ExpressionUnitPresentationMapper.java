package local.myproject.scalc.presentation.mapper;

import local.myproject.scalc.domain.ExpressionUnit;
import local.myproject.scalc.domain.Project;
import local.myproject.scalc.presentation.dto.ExpressionUnitDto;
import org.springframework.stereotype.Component;

@Component
public class ExpressionUnitPresentationMapper {
    public ExpressionUnitDto toDto(ExpressionUnit model) {
        ExpressionUnitDto dto = new ExpressionUnitDto();
        dto.setExpressionUnitId(model.getExpressionUnitId());
        dto.setTypeOfOperand(model.getTypeOfOperand());
        dto.setExpressionUnitName(model.getExpressionUnitName());
        dto.setDefaultExpression(model.getDefaultExpression());
        dto.setExpressionResult(model.getExpressionResult());
        dto.setProjectId(model.getProject() == null ? null : model.getProject().getProjectId());
        dto.setWatchList(model.isWatchList());
        try {
            dto.setUnitExpression(model.getUnitExpression());
            dto.setAtomicExpressions(model.getAtomicExpressions());
        } catch (Exception exception) {
            throw new IllegalStateException("Не удалось подготовить DTO выражения", exception);
        }
        return dto;
    }

    public ExpressionUnit toDomain(ExpressionUnitDto dto) {
        ExpressionUnit expressionUnit = new ExpressionUnit();
        expressionUnit.setExpressionUnitId(dto.getExpressionUnitId());
        expressionUnit.setTypeOfOperand(dto.getTypeOfOperand());
        expressionUnit.setExpressionUnitName(dto.getExpressionUnitName());
        expressionUnit.setExpressionResult(dto.getExpressionResult());
        expressionUnit.setWatchList(dto.isWatchList());
        if (dto.getProjectId() != null) {
            Project project = new Project();
            project.setProjectId(dto.getProjectId());
            expressionUnit.setProject(project);
        }
        try {
            expressionUnit.setDefaultExpression(dto.getDefaultExpression() == null ? "" : dto.getDefaultExpression());
        } catch (Exception exception) {
            throw new IllegalStateException("Не удалось восстановить выражение из DTO", exception);
        }
        return expressionUnit;
    }
}
