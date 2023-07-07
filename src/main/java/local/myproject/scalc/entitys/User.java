package local.myproject.scalc.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Size(min = 3, max = 15, message = "Required name from 3 to 15 chars")
    private String userName;
    @Size(min = 7, message = "Required password minimum 7 chars")
    private String password;
    @Transient
    private String confirmPassword;
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email isn't correct")
    private String email;

    @CollectionTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")})
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Project> projects;

    public String validatePassword() {
        if(!password.equals(confirmPassword)) {
            return "Passwords is not equals";
        }
        return "";
    }

}
