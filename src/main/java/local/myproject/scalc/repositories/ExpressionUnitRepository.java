package local.myproject.scalc.repositories;

import local.myproject.scalc.entitys.ExpressionUnit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpressionUnitRepository extends CrudRepository<ExpressionUnit, Integer> {

    @Override
    List<ExpressionUnit> findAll();

    @Query(value = "SELECT e FROM ExpressionUnit e WHERE e.project.projectId = :projectId")
    List<ExpressionUnit> findAllByProjectId(@Param("projectId") int projectId);

    @Query(value = "SELECT e.expressionUnitName FROM ExpressionUnit e WHERE e.project.projectId = :projectId")
    List<String> findAllNameByProjectId(@Param("projectId") int projectId);

}
