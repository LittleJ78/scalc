package local.myproject.scalc.persistent.dao;

import local.myproject.scalc.domain.ExpressionUnit;

import java.util.List;
import java.util.Optional;

public interface ExpressionUnitDao {
    List<ExpressionUnit> findAll();
    List<ExpressionUnit> findAllByProjectId(int projectId);
    List<String> findAllNameByProjectId(int projectId);
    Optional<ExpressionUnit> findById(int expressionUnitId);
    ExpressionUnit save(ExpressionUnit expressionUnit);
    ExpressionUnit update(ExpressionUnit expressionUnit);
    void deleteById(int expressionUnitId);
}
