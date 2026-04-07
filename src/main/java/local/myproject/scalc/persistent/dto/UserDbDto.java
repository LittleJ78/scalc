package local.myproject.scalc.persistent.dto;

import lombok.Data;

@Data
public class UserDbDto {
    private Long userId;
    private String userName;
    private String password;
    private String email;
}
