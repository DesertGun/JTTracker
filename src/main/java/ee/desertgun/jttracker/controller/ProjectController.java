package ee.desertgun.jttracker.controller;



import ee.desertgun.jttracker.domain.UserProject;
import ee.desertgun.jttracker.dto.UserProjectDTO;
import ee.desertgun.jttracker.dto.UserProjectTimeDTO;
import ee.desertgun.jttracker.service.UserProjectService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(path = "/api")
@PreAuthorize("hasRole('ROLE_USER')")
public class ProjectController {

    private final UserProjectService userProjectService;

    public ProjectController(UserProjectService userProjectService) {
        this.userProjectService = userProjectService;
    }

    @GetMapping("/project")
    public List<UserProject> getUserProjects(Authentication authentication) {
        return userProjectService.getUserProjects(authentication.getName());
    }

    @GetMapping("/project/{projectID}")
    public UserProject getUserProject(@PathVariable UUID projectID) {
        return userProjectService.getProject(projectID);
    }

    @DeleteMapping("/project/{projectID}")
    public void deleteProject(@PathVariable UUID projectID) {
        userProjectService.deleteUserProject(projectID);
    }

    @PatchMapping("/project/{projectID}")
    public void updateProject(@PathVariable UUID projectID, @RequestBody @Valid UserProjectDTO userProjectDTO) {
        userProjectService.updateUserProject(projectID, userProjectDTO);
    }

    @PatchMapping("/project/time/{projectID}")
    public void updateProjectTime(@PathVariable UUID projectID, @RequestBody @Valid UserProjectTimeDTO userProjectTimeDTO) {
        userProjectService.addTimeToProject(projectID, userProjectTimeDTO.getTrackedTimeList(),
                userProjectTimeDTO.getProjectTime());
    }

    @PostMapping("/project")
    public void addProject(@RequestBody @Valid UserProjectDTO userProjectDTO, Authentication authentication) {
        userProjectService.createUserProject(userProjectDTO, authentication.getName());
    }
}
