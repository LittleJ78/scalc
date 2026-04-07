package local.myproject.scalc.persistent.dao;

import local.myproject.scalc.domain.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectDao {
    List<Project> findAllByUserId(Long userId);
    Optional<Project> findById(int projectId);
    Project save(Project project);
    Project update(Project project);
    void deleteById(int projectId);
}
