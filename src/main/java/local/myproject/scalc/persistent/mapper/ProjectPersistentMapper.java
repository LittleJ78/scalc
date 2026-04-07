package local.myproject.scalc.persistent.mapper;

import local.myproject.scalc.domain.Project;
import local.myproject.scalc.domain.User;
import local.myproject.scalc.persistent.dto.ProjectDbDto;
import org.springframework.stereotype.Component;

@Component
public class ProjectPersistentMapper {
    public Project toEntity(ProjectDbDto dto) {
        Project project = new Project();
        project.setProjectId(dto.getProjectId() == null ? 0 : dto.getProjectId());
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        if (dto.getUserId() != null) {
            User user = new User();
            user.setUserId(dto.getUserId());
            project.setUser(user);
        }
        return project;
    }

    public ProjectDbDto toDto(Project project) {
        ProjectDbDto dto = new ProjectDbDto();
        dto.setProjectId(project.getProjectId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setUserId(project.getUser() == null ? null : project.getUser().getUserId());
        return dto;
    }
}
