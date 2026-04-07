package local.myproject.scalc.presentation.mapper;

import local.myproject.scalc.domain.Project;
import local.myproject.scalc.domain.User;
import local.myproject.scalc.presentation.dto.ProjectDto;
import org.springframework.stereotype.Component;

@Component
public class ProjectPresentationMapper {
    public ProjectDto toDto(Project model) {
        ProjectDto dto = new ProjectDto();
        dto.setProjectId(model.getProjectId());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        dto.setUserId(model.getUser() == null ? null : model.getUser().getUserId());
        return dto;
    }

    public Project toDomain(ProjectDto dto) {
        Project project = new Project();
        project.setProjectId(dto.getProjectId());
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        if (dto.getUserId() != null) {
            User user = new User();
            user.setUserId(dto.getUserId());
            project.setUser(user);
        }
        return project;
    }
}
