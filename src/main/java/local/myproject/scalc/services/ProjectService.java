package local.myproject.scalc.services;

import local.myproject.scalc.entitys.ExpressionUnit;
import local.myproject.scalc.entitys.Project;
import local.myproject.scalc.repositories.ProjectRepository;

import java.util.List;

public interface ProjectService {


    List<Project> findAllByUser(Long userId);
    Project findById(int projectId);
    void save(Project project);
    void update(Project project);
    void deleteById(int projectId);
}
