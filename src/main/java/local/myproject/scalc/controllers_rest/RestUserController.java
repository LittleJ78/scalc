package local.myproject.scalc.controllers_rest;

import jakarta.validation.Valid;
import local.myproject.scalc.entitys.User;
import local.myproject.scalc.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class RestUserController {
    private final UserServiceImpl userService;

    @ModelAttribute
    public User newUser() {
        return new User();
    }

    @GetMapping
    public User newUserRegistration(User user) {
        return user;
    }

    @PostMapping
    public User saveNewUser(@Valid @RequestBody User user, BindingResult errors) {
        if(!user.validatePassword().isEmpty())  {
            ObjectError error = new ObjectError("confirmPasswordError", user.validatePassword());
            errors.addError(error);
        }
        if(errors.hasErrors()) {
            user.setUserName("ERROR");
            return user;
        }
        userService.save(user);
        return user;
    }

    @PutMapping("{id}")
    public User updateUser(@PathVariable long id, @RequestBody User user, BindingResult errors) {
        user.setUserId(id);
        userService.updateUser(user);
        return user;
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteById(id);
    }
}
