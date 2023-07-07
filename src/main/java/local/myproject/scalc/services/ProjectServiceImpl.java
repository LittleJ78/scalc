package local.myproject.scalc.services;

import local.myproject.scalc.entitys.Project;
import local.myproject.scalc.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    public final ProjectRepository projectRepository;

    @Override
    public List<Project> findAllByUser(Long userId) {
        return (List<Project>) projectRepository.findAllByUserId(userId);
    }

    @Override
    public Project findById(int projectId) {
        return projectRepository.findById(projectId).get();
    }

    @Override
    public void save(Project project) {
        projectRepository.save(project);
    }

    @Override
    public void update(Project project) {
        projectRepository.save(project);
    }

    @Override
    public void deleteById(int projectId) {
        projectRepository.deleteById(projectId);
    }
}
