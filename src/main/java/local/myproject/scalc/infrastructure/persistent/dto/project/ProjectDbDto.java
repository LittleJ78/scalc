package local.myproject.scalc.infrastructure.persistent.dto.project;

/**
 * DTO строки таблицы проектов.
 *
 * @author Evgenii Mironov
 */
public record ProjectDbDto(
        Integer projectId,
        String name,
        String description,
        Long userId
) {
}
