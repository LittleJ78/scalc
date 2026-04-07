package local.myproject.scalc.persistent.dto;

import lombok.Data;

@Data
public class ProjectDbDto {
    private Integer projectId;
    private String name;
    private String description;
    private Long userId;
}
