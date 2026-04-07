package local.myproject.scalc.persistent.mapper;

import local.myproject.scalc.domain.ExpressionUnit;
import local.myproject.scalc.domain.Project;
import local.myproject.scalc.persistent.dto.ExpressionUnitDbDto;
import org.springframework.stereotype.Component;

@Component
public class ExpressionUnitPersistentMapper {
    public ExpressionUnit toEntity(ExpressionUnitDbDto dto) {
        ExpressionUnit expressionUnit = new ExpressionUnit();
        expressionUnit.setExpressionUnitId(dto.getExpressionUnitId() == null ? 0 : dto.getExpressionUnitId());
        expressionUnit.setTypeOfOperand(dto.getTypeOfOperand());
        expressionUnit.setExpressionUnitName(dto.getExpressionUnitName());
        expressionUnit.setExpressionResult(dto.getExpressionResult());
        expressionUnit.setWatchList(dto.isWatchList());
        Project project = new Project();
        project.setProjectId(dto.getProjectId() == null ? 0 : dto.getProjectId());
        expressionUnit.setProject(project);
        try {
            expressionUnit.setDefaultExpression(dto.getDefaultExpression());
        } catch (Exception exception) {
            throw new IllegalStateException("Не удалось восстановить выражение из базы данных", exception);
        }
        return expressionUnit;
    }

    public ExpressionUnitDbDto toDto(ExpressionUnit expressionUnit) {
        ExpressionUnitDbDto dto = new ExpressionUnitDbDto();
        dto.setExpressionUnitId(expressionUnit.getExpressionUnitId());
        dto.setTypeOfOperand(expressionUnit.getTypeOfOperand());
        dto.setExpressionUnitName(expressionUnit.getExpressionUnitName());
        dto.setDefaultExpression(expressionUnit.getDefaultExpression());
        dto.setExpressionResult(expressionUnit.getExpressionResult());
        dto.setProjectId(expressionUnit.getProject() == null ? null : expressionUnit.getProject().getProjectId());
        dto.setWatchList(expressionUnit.isWatchList());
        return dto;
    }
}
