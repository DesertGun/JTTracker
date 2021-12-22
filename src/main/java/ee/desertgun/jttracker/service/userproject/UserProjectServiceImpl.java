package ee.desertgun.jttracker.service.userproject;

import ee.desertgun.jttracker.domain.TrackedTime;
import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.domain.UserProject;
import ee.desertgun.jttracker.dto.UserProjectDTO;
import ee.desertgun.jttracker.repository.ProjectRepository;
import ee.desertgun.jttracker.repository.TrackedTimeRepository;
import ee.desertgun.jttracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class UserProjectServiceImpl implements UserProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TrackedTimeRepository trackedTimeRepository;

    public UserProjectServiceImpl(final ProjectRepository projectRepository, UserRepository userRepository, TrackedTimeRepository trackedTimeRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.trackedTimeRepository = trackedTimeRepository;
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
    public void deleteUserProject(UUID projectID, String username) {
        UserProject userProject = projectRepository.getByProjectID(projectID);
        List<TrackedTime> trackedTimeList = trackedTimeRepository.getTrackedTimesByUser(userRepository.findByUsername(username));
        for (TrackedTime trackedTime : trackedTimeList) {
            if (trackedTime.getProjectList().contains(userProject)) {
                trackedTime.removeProject(userProject);
                trackedTimeRepository.save(trackedTime);
                userProject.removeTime(trackedTime);
            }
        }
        projectRepository.delete(userProject);
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
    public void addTimeToProject(UUID projectID, List<TrackedTime> trackedTimeList, UUID trackedTimeID, Duration projectTime) {
        UserProject userProject = projectRepository.getByProjectID(projectID);
        TrackedTime trackedTimeToAdd = trackedTimeRepository.getTrackedTimeByTimeID(trackedTimeID);
        if (trackedTimeToAdd.getProjectList().contains(userProject)) {
            trackedTimeToAdd.getProjectList().remove(userProject);
        } else {
            trackedTimeToAdd.getProjectList().add(userProject);
        }
        userProject.setTrackedTimeList(trackedTimeList);
        userProject.setProjectTime(projectTime);
        projectRepository.save(userProject);
        trackedTimeRepository.save(trackedTimeToAdd);
    }

    @Override
    public UserProject getProject(UUID projectID) {
        return projectRepository.getByProjectID(projectID);
    }
}