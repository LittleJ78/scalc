package local.myproject.scalc.repositories;

import local.myproject.scalc.entitys.ParametersForExpressions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametersForExpressionsRepository extends CrudRepository<ParametersForExpressions,Integer> {

}
