package local.myproject.scalc.controllers;

import jakarta.validation.Valid;
import local.myproject.scalc.entitys.User;
import local.myproject.scalc.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userService;

    @ModelAttribute
    public User newUser() {
        return new User();
    }

    @GetMapping
    public String newUserRegistration() {
        return "user/new";
    }

    @PostMapping
    public String saveNewUser(@Valid User user, BindingResult errors) {
        if(!user.validatePassword().isEmpty())  {
            ObjectError error = new ObjectError("confirmPasswordError", user.validatePassword());
            errors.addError(error);
        }
        if(errors.hasErrors()) {
            return "user/new";
        }
        userService.save(user);
        return "redirect:/";
    }

}
