package local.myproject.scalc.services;

import local.myproject.scalc.domain.Project;
import local.myproject.scalc.domain.User;
import local.myproject.scalc.presentation.dto.ProjectDto;
import local.myproject.scalc.presentation.mapper.ProjectPresentationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectApiService {
    private final ProjectServiceImpl projectService;
    private final UserServiceImpl userService;
    private final ProjectPresentationMapper projectPresentationMapper;

    public List<ProjectDto> findAll(String userName) {
        User user = getCurrentUser(userName);
        return projectService.findAllByUser(user.getUserId()).stream()
                .map(projectPresentationMapper::toDto)
                .toList();
    }

    public ProjectDto create(String userName, ProjectDto request) {
        User user = getCurrentUser(userName);
        Project project = projectPresentationMapper.toDomain(request);
        project.setUser(user);
        projectService.save(project);
        return projectPresentationMapper.toDto(project);
    }

    public ProjectDto findById(String userName, int projectId) {
        return projectPresentationMapper.toDto(getOwnedProject(userName, projectId));
    }

    public ProjectDto update(String userName, int projectId, ProjectDto request) {
        Project project = getOwnedProject(userName, projectId);
        Project update = projectPresentationMapper.toDomain(request);
        project.setName(update.getName());
        project.setDescription(update.getDescription());
        projectService.update(project);
        return projectPresentationMapper.toDto(project);
    }

    public void delete(String userName, int projectId) {
        getOwnedProject(userName, projectId);
        projectService.deleteById(projectId);
    }

    Project getOwnedProject(String userName, int projectId) {
        User user = getCurrentUser(userName);
        Project project = projectService.findById(projectId);
        if (project.getUser() == null || !project.getUser().getUserId().equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        return project;
    }

    private User getCurrentUser(String userName) {
        return userService.findByUserName(userName);
    }
}
