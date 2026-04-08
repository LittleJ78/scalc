package local.myproject.scalc.infrastructure.persistent.mapper.project;

import local.myproject.scalc.domain.aggregate.project.Project;
import local.myproject.scalc.domain.aggregate.user.User;
import local.myproject.scalc.infrastructure.persistent.dto.project.ProjectDbDto;
import org.springframework.stereotype.Component;

/**
 * Маппер проектов между доменной моделью и persistence DTO.
 *
 * @author Evgenii Mironov
 */
@Component
public class ProjectPersistentMapper {
    /**
     * Преобразует DTO базы данных в доменную модель проекта.
     *
     * @param dto DTO строки базы данных
     * @return доменная модель проекта
     */
    public Project toEntity(ProjectDbDto dto) {
        Project project = new Project();
        project.setProjectId(dto.projectId() == null ? 0 : dto.projectId());
        project.setName(dto.name());
        project.setDescription(dto.description());
        if (dto.userId() != null) {
            User user = new User();
            user.setUserId(dto.userId());
            project.setUser(user);
        }
        return project;
    }

    /**
     * Преобразует доменную модель проекта в DTO базы данных.
     *
     * @param project доменная модель проекта
     * @return DTO базы данных
     */
    public ProjectDbDto toDto(Project project) {
        return new ProjectDbDto(
                project.getProjectId(),
                project.getName(),
                project.getDescription(),
                project.getUser() == null ? null : project.getUser().getUserId()
        );
    }
}
