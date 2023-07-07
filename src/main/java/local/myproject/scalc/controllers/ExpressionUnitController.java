package local.myproject.scalc.controllers;

import jakarta.validation.Valid;
import local.myproject.scalc.entitys.ExpressionUnit;
import local.myproject.scalc.entitys.Project;
import local.myproject.scalc.services.ExpressionUnitServiceImpl;
import local.myproject.scalc.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/expressions")
@SessionAttributes("project")
public class ExpressionUnitController {

    private ExpressionUnit currenExpressionUnit;
    private final ExpressionUnitServiceImpl expressionUnitService;
    private final UserServiceImpl userService;

    @ModelAttribute(name = "watchList")
    public List<ExpressionUnit> getWatchList(@ModelAttribute("project") Project project) {
        return expressionUnitService.createWatchList(project.getProjectId());
    }

    @ModelAttribute(name="expressionUnit")
    public ExpressionUnit createExpression(String typeOfOperand){
        return new ExpressionUnit(typeOfOperand);
    }

    @ModelAttribute(name="expressionList")
    public List<ExpressionUnit> createListOfAllExpression(@ModelAttribute("project") Project project) {
        return expressionUnitService.findAllByProjectId(project.getProjectId());
    }

    @ModelAttribute(name = "user")
    public local.myproject.scalc.entitys.User getActiveUser(@AuthenticationPrincipal User user) {
        return userService.findByUserName(user.getUsername());
    }

    @GetMapping()
    public String index(@ModelAttribute("project") Project project) {
        log.info("Start request - GET");
        if (project.getProjectId() == 0) {
            return "redirect:/project";
        }
        return "expressions/index";
    }

    /**
     * метод вычисляет выражение
     * @param modelExpression Объект класса ExpressionUnit с формы ввода
     * @param errors обработчик ошибок
     */
    @PostMapping()
    public String calculateExpression(@Valid ExpressionUnit modelExpression, Errors errors,  @ModelAttribute Project project) {
        log.info("PostMapping()");
        if(errors.hasErrors()) {
            log.warn("nu a che ne vvodim?, {}",errors.getFieldError().getDefaultMessage());
        }

        modelExpression.setProject(project);
        currenExpressionUnit = modelExpression;
        return "expressions/index";
    }

    /**
     * добавляет выражение в лист проекта
     */
    @PatchMapping()
    public String addExpression() {
            expressionUnitService.save(currenExpressionUnit);
        return "redirect:/expressions";
    }

    @GetMapping("/{expressionUnitId}")
    public String editExpression(@PathVariable int expressionUnitId, Model model, @ModelAttribute("project") Project project) {
        if (project.getProjectId() == 0) {
            return "redirect:/project";
        }
        model.addAttribute("expressionUnit", expressionUnitService.findById(expressionUnitId));
        currenExpressionUnit = expressionUnitService.findById(expressionUnitId);
        currenExpressionUnit.setProject(project);
        return "expressions/update";
    }
    @PostMapping("/{expressionUnitId}")
    public String calculateEditedExpression(@Valid ExpressionUnit modelExpression, Errors errors, Model model, @ModelAttribute("project") Project project) {
        if(errors.hasErrors()) {
            log.warn("nu a che ne vvodim?");
        }
        currenExpressionUnit = modelExpression;
        currenExpressionUnit.setProject(project);
        return "expressions/update";
    }
    @PatchMapping("/{expressionUnitId}")
    public String updateExpression() {
        expressionUnitService.update(currenExpressionUnit);
        return "redirect:/expressions";
    }
    @DeleteMapping("/{expressionUnitId}")
    public String deleteExpression(@PathVariable int expressionUnitId, @ModelAttribute("project") Project project) {
        expressionUnitService.deleteById(expressionUnitId);
        return "redirect:/expressions";
    }
}

