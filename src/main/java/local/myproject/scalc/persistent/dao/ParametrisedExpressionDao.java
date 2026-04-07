package local.myproject.scalc.persistent.dao;

import local.myproject.scalc.domain.ParametrisedExpressions;

import java.util.Optional;

public interface ParametrisedExpressionDao {
    Optional<ParametrisedExpressions> findByExpressionUnitId(int expressionUnitId);
    ParametrisedExpressions save(ParametrisedExpressions parametrisedExpressions);
    ParametrisedExpressions update(ParametrisedExpressions parametrisedExpressions);
    void deleteById(int parametrisedExpressionId);
}
