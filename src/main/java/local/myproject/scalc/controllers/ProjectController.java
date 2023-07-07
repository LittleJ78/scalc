package local.myproject.scalc.controllers;

import local.myproject.scalc.entitys.ExpressionUnit;
import local.myproject.scalc.entitys.Project;
import local.myproject.scalc.services.ExpressionUnitServiceImpl;
import local.myproject.scalc.services.ProjectServiceImpl;
import local.myproject.scalc.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/project")
@RequiredArgsConstructor
@SessionAttributes(value = "project")
public class ProjectController {
    private final ProjectServiceImpl projectService;
    private final ExpressionUnitServiceImpl expressionUnitService;
    private final UserServiceImpl userService;


    @ModelAttribute(name="projectList")
    public List<Project> getListOfProjects(@AuthenticationPrincipal User user) {
        return projectService.findAllByUser(userService.findByUserName(user.getUsername()).getUserId());
    }

    @ModelAttribute(name = "user")
    public local.myproject.scalc.entitys.User getActiveUser(@AuthenticationPrincipal User user) {
        return userService.findByUserName(user.getUsername());
    }

    @GetMapping()
    public String index() throws Exception {
        return "project/index";
    }

    @GetMapping("/new")
    public String createNewProject(@ModelAttribute("newProject") Project project) {
        return "project/new";
    }
    @PostMapping()
    public String saveNewProject(@ModelAttribute("newProject") Project project, @AuthenticationPrincipal User user) {
        project.setUser(userService.findByUserName(user.getUsername()));
        projectService.save(project);
        return "redirect:/project";
    }
    @GetMapping("/{projectId}")
    public String editExpression(@PathVariable int projectId, Model model) {
        model.addAttribute("project", projectService.findById(projectId));
        model.addAttribute("watchList", expressionUnitService.createWatchList(projectId));
        return "project/show";
    }
    @GetMapping("/{projectId}/update")
    public String edit(Model model, @PathVariable int projectId) {
        model.addAttribute("project", projectService.findById(projectId));
        return "project/update";
    }
    @PatchMapping("/{projectId}/update")
    public String updateProject(@PathVariable int projectId, Project project) {
        projectService.update(project);
        return "redirect:/project/" + projectId;
    }
    @DeleteMapping("/{projectId}")
    public String deleteProject(@PathVariable int projectId) {
        projectService.deleteById(projectId);
        return "redirect:/project";
    }
}
