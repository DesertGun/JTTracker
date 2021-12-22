package ee.desertgun.jttracker.controller;


import ee.desertgun.jttracker.domain.UserProject;
import ee.desertgun.jttracker.dto.UserProjectDTO;
import ee.desertgun.jttracker.dto.UserProjectTimeDTO;
import ee.desertgun.jttracker.service.userproject.UserProjectService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
public class ProjectController {

    private final UserProjectService userProjectService;

    public ProjectController(UserProjectService userProjectService) {
        this.userProjectService = userProjectService;
    }

    @GetMapping("/project")
    public List<UserProject> getUserProjects(Principal principal) {
        return userProjectService.getUserProjects(principal.getName());
    }

    @GetMapping("/project/{projectID}")
    public UserProject getUserProject(@PathVariable UUID projectID) {
        return userProjectService.getProject(projectID);
    }

    @DeleteMapping("/project/{projectID}")
    public void deleteProject(@PathVariable UUID projectID, Principal principal) {
        userProjectService.deleteUserProject(projectID, principal.getName());
    }

    @PatchMapping("/project/{projectID}")
    public void updateProject(@PathVariable UUID projectID, @RequestBody @Valid UserProjectDTO userProjectDTO) {
        userProjectService.updateUserProject(projectID, userProjectDTO);
    }

    @PatchMapping("/project/time/{projectID}")
    public void updateProjectTime(@PathVariable UUID projectID, @RequestBody @Valid UserProjectTimeDTO userProjectTimeDTO) {
        userProjectService.addTimeToProject(projectID, userProjectTimeDTO.getTrackedTimeList(), userProjectTimeDTO.getTrackedTimeID(),
                userProjectTimeDTO.getProjectTime());
    }

    @PostMapping("/project")
    public void addProject(@RequestBody @Valid UserProjectDTO userProjectDTO, Principal principal) {
        userProjectService.createUserProject(userProjectDTO, principal.getName());
    }
}
