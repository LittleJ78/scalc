package local.myproject.scalc.presentation.mapper.project;

import local.myproject.scalc.domain.aggregate.project.Project;
import local.myproject.scalc.domain.aggregate.user.User;
import local.myproject.scalc.presentation.dto.project.ProjectDto;
import org.springframework.stereotype.Component;

/**
 * Маппер проектов между доменной моделью и REST DTO.
 *
 * @author Evgenii Mironov
 */
@Component
public class ProjectPresentationMapper {
    /**
     * Преобразует доменную модель проекта в DTO.
     *
     * @param model доменная модель проекта
     * @return DTO проекта
     */
    public ProjectDto toDto(Project model) {
        return new ProjectDto(
                model.getProjectId(),
                model.getName(),
                model.getDescription(),
                model.getUser() == null ? null : model.getUser().getUserId()
        );
    }

    /**
     * Преобразует DTO проекта в доменную модель.
     *
     * @param dto DTO проекта
     * @return доменная модель проекта
     */
    public Project toDomain(ProjectDto dto) {
        Project project = new Project();
        project.setProjectId(dto.projectId());
        project.setName(dto.name());
        project.setDescription(dto.description());
        if (dto.userId() != null) {
            User user = new User();
            user.setUserId(dto.userId());
            project.setUser(user);
        }
        return project;
    }
}
