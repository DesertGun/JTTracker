package ee.desertgun.jttracker.service.userproject;

import ee.desertgun.jttracker.domain.TrackedTime;
import ee.desertgun.jttracker.domain.UserProject;
import ee.desertgun.jttracker.dto.UserProjectDTO;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public interface UserProjectService {

    void createUserProject(UserProjectDTO userProjectDTO, String username);

    void deleteUserProject(UUID projectID);

    void updateUserProject(UUID projectID, UserProjectDTO userProjectDTO);

    List<UserProject> getUserProjects(String username);

    void addTimeToProject(UUID projectID, List<TrackedTime> trackedTimeList, Duration projectTime);


    UserProject getProject(UUID projectID);
}
