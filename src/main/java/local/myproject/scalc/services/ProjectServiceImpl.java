package local.myproject.scalc.services;

import local.myproject.scalc.domain.Project;
import local.myproject.scalc.persistent.dao.ProjectDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    public final ProjectDao projectDao;

    @Override
    public List<Project> findAllByUser(Long userId) {
        return projectDao.findAllByUserId(userId);
    }

    @Override
    public Project findById(int projectId) {
        return projectDao.findById(projectId).orElseThrow();
    }

    @Override
    public void save(Project project) {
        projectDao.save(project);
    }

    @Override
    public void update(Project project) {
        projectDao.update(project);
    }

    @Override
    public void deleteById(int projectId) {
        projectDao.deleteById(projectId);
    }
}
