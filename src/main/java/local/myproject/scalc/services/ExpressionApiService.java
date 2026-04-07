package local.myproject.scalc.services;

import local.myproject.scalc.domain.ExpressionUnit;
import local.myproject.scalc.domain.Project;
import local.myproject.scalc.domain.User;
import local.myproject.scalc.presentation.dto.ExpressionUnitDto;
import local.myproject.scalc.presentation.mapper.ExpressionUnitPresentationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpressionApiService {
    private final ExpressionUnitServiceImpl expressionUnitService;
    private final ProjectServiceImpl projectService;
    private final UserServiceImpl userService;
    private final ExpressionUnitPresentationMapper expressionUnitPresentationMapper;

    public List<ExpressionUnitDto> findAllByProject(String userName, int projectId) {
        Project project = getOwnedProject(userName, projectId);
        return expressionUnitService.findAllByProjectId(project.getProjectId()).stream()
                .map(expressionUnitPresentationMapper::toDto)
                .toList();
    }

    public ExpressionUnitDto calculate(String userName, int projectId, ExpressionUnitDto request) throws Exception {
        Project project = getOwnedProject(userName, projectId);
        ExpressionUnit requestDomain = expressionUnitPresentationMapper.toDomain(request);
        ExpressionUnit expressionUnit = toExpressionUnit(requestDomain, project);
        return expressionUnitPresentationMapper.toDto(expressionUnit);
    }

    public ExpressionUnitDto create(String userName, int projectId, ExpressionUnitDto request) throws Exception {
        Project project = getOwnedProject(userName, projectId);
        ExpressionUnit expressionUnit = toExpressionUnit(expressionUnitPresentationMapper.toDomain(request), project);
        expressionUnit.getUnitExpression();
        expressionUnitService.save(expressionUnit);
        return expressionUnitPresentationMapper.toDto(expressionUnit);
    }

    public ExpressionUnitDto findById(String userName, int expressionUnitId) {
        return expressionUnitPresentationMapper.toDto(getOwnedExpression(userName, expressionUnitId));
    }

    public ExpressionUnitDto update(String userName, int expressionUnitId, ExpressionUnitDto request) throws Exception {
        ExpressionUnit expressionUnit = getOwnedExpression(userName, expressionUnitId);
        ExpressionUnit requestDomain = expressionUnitPresentationMapper.toDomain(request);
        expressionUnit.setTypeOfOperand(defaultOperandType(requestDomain.getTypeOfOperand()));
        if (requestDomain.getExpressionUnitName() != null && !requestDomain.getExpressionUnitName().isBlank()) {
            expressionUnit.setExpressionUnitName(requestDomain.getExpressionUnitName());
        }
        expressionUnit.setDefaultExpression(requestDomain.getDefaultExpression() == null ? "" : requestDomain.getDefaultExpression());
        expressionUnit.setWatchList(requestDomain.isWatchList());
        expressionUnit.getUnitExpression();
        expressionUnitService.update(expressionUnit);
        return expressionUnitPresentationMapper.toDto(expressionUnit);
    }

    public void delete(String userName, int expressionUnitId) {
        getOwnedExpression(userName, expressionUnitId);
        expressionUnitService.deleteById(expressionUnitId);
    }

    private ExpressionUnit toExpressionUnit(ExpressionUnit request, Project project) throws Exception {
        ExpressionUnit expressionUnit = new ExpressionUnit(defaultOperandType(request.getTypeOfOperand()));
        if (request.getExpressionUnitName() != null && !request.getExpressionUnitName().isBlank()) {
            expressionUnit.setExpressionUnitName(request.getExpressionUnitName());
        }
        expressionUnit.setDefaultExpression(request.getDefaultExpression() == null ? "" : request.getDefaultExpression());
        expressionUnit.setWatchList(request.isWatchList());
        expressionUnit.setProject(project);
        return expressionUnit;
    }

    private String defaultOperandType(String typeOfOperand) {
        return typeOfOperand == null || typeOfOperand.isBlank() ? "Default" : typeOfOperand;
    }

    private User getCurrentUser(String userName) {
        return userService.findByUserName(userName);
    }

    private Project getOwnedProject(String userName, int projectId) {
        User user = getCurrentUser(userName);
        Project project = projectService.findById(projectId);
        if (project.getUser() == null || !project.getUser().getUserId().equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        return project;
    }

    private ExpressionUnit getOwnedExpression(String userName, int expressionUnitId) {
        User user = getCurrentUser(userName);
        ExpressionUnit expressionUnit = expressionUnitService.findById(expressionUnitId);
        if (expressionUnit.getProject() == null
                || expressionUnit.getProject().getUser() == null
                || !expressionUnit.getProject().getUser().getUserId().equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expression not found");
        }
        return expressionUnit;
    }
}
