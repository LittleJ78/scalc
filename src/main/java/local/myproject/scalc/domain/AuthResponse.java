package local.myproject.scalc.domain;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String tokenType;
    private long expiresIn;
    private User user;
}
