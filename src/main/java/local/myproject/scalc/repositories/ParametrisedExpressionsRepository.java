package local.myproject.scalc.repositories;

import local.myproject.scalc.entitys.ParametrisedExpressions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParametrisedExpressionsRepository extends CrudRepository<ParametrisedExpressions,Integer> {

    @Query(value = "SELECT p FROM ParametrisedExpressions p WHERE p.expressionUnit.expressionUnitId = :expressionUnitId")
    List<ParametrisedExpressions> FindByExpressionUnitId(@Param("expressionUnitId") int expressionUnitId);
}
