package local.myproject.scalc.services;

import local.myproject.scalc.domain.Project;

import java.util.List;

public interface ProjectService {


    List<Project> findAllByUser(Long userId);
    Project findById(int projectId);
    void save(Project project);
    void update(Project project);
    void deleteById(int projectId);
}
