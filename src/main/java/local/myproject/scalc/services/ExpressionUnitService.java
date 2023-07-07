package local.myproject.scalc.services;

import local.myproject.scalc.entitys.ExpressionUnit;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpressionUnitService {

    ExpressionUnit findById(int expressionUnitId);
    List<ExpressionUnit> findAllByProjectId(int projectId);

    List<ExpressionUnit> createWatchList(int projectId);
    void save(ExpressionUnit expressionUnit);
    void update(ExpressionUnit expressionUnit);
    void deleteById(int expressionUnitId);
}
