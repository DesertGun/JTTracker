package ee.desertgun.jttracker.service;

import ee.desertgun.jttracker.domain.TrackedTime;
import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.domain.UserProject;
import ee.desertgun.jttracker.dto.UserProjectDTO;
import ee.desertgun.jttracker.repository.ProjectRepository;
import ee.desertgun.jttracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class UserProjectServiceImpl implements UserProjectService {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    public UserProjectServiceImpl(final ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createUserProject(UserProjectDTO userProjectDTO, String username) {
        User user = userRepository.findByUsername(username);
        UserProject userProject = new UserProject();
        userProject.setUser(user);
        userProject.setProjectName(userProjectDTO.getProjectName());
        userProject.setProjectDesc(userProjectDTO.getProjectDesc());
        userProject.setStatus(userProjectDTO.getStatus());
        userProject.setPriority(userProjectDTO.getPriority());
        userProject.setProjectID(userProjectDTO.getProjectID());
        projectRepository.save(userProject);
    }

    @Override
    public void deleteUserProject(UUID projectID) {
        projectRepository.delete(projectRepository.getByProjectID(projectID));
    }

    @Override
    public void updateUserProject(UUID projectID, UserProjectDTO userProjectDTO) {
        UserProject userProject = projectRepository.getByProjectID(projectID);
        userProject.setProjectName(userProjectDTO.getProjectName());
        userProject.setProjectDesc(userProjectDTO.getProjectDesc());
        userProject.setStatus(userProjectDTO.getStatus());
        userProject.setPriority(userProjectDTO.getPriority());
        projectRepository.save(userProject);
    }

    @Override
    public List<UserProject> getUserProjects(String username) {
        return projectRepository.getAllByUser(userRepository.findByUsername(username));
    }

    @Override
    public void addTimeToProject(UUID projectID, List<TrackedTime> trackedTimeList, Duration projectTime) {
        UserProject userProject = projectRepository.getByProjectID(projectID);
        userProject.setTrackedTimeList(trackedTimeList);
        userProject.setProjectTime(projectTime);
        projectRepository.save(userProject);
    }

    @Override
    public UserProject getProject(UUID projectID) {
        return projectRepository.getByProjectID(projectID);
    }
}