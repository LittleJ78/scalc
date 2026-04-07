package local.myproject.scalc.domain;

import lombok.Data;

@Data
public class RegisterRequest {
    private String userName;
    private String password;
    private String confirmPassword;
    private String email;
}
