package local.myproject.scalc.repositories;

import local.myproject.scalc.entitys.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Integer> {
    @Query(value = "SELECT p FROM  Project p WHERE p.user.userId  = :userId")
    List<Project> findAllByUserId(Long userId);
}
