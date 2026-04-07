package local.myproject.scalc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class User {
    private Long userId;

    @Size(min = 3, max = 15, message = "Required name from 3 to 15 chars")
    private String userName;

    @Size(min = 7, message = "Required password minimum 7 chars")
    private String password;

    private String confirmPassword;

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email isn't correct")
    private String email;

    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    private List<Project> projects;

    public String validatePassword() {
        if (!password.equals(confirmPassword)) {
            return "Passwords is not equals";
        }
        return "";
    }
}
