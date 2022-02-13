package ee.desertgun.jttracker.domain;

import ee.desertgun.jttracker.repository.ProjectRepository;
import ee.desertgun.jttracker.repository.TrackedTimeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserProjectTest {

    private User user;
    private TrackedTime trackedTime;
    private TrackedTime secondTrackedTime;
    private UserProject userProject;

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TrackedTimeRepository trackedTimeRepository;

    @Autowired
    ProjectRepository projectRepository;


    @Before
    public void createTestUser() {
        User user = new User("");
        user.setUsername("test");
        user = entityManager.persistAndFlush(user);

        this.user = user;
    }


    @Before
    public void createTestTimeRecord() {
        Instant start = Instant.parse("2019-12-03T10:15:30.00Z");
        Instant end = Instant.parse("2019-12-03T13:15:30.00Z");
        Duration duration = java.time.Duration.between(start, end);
        String timeDesc = "Start";
        UUID timeID = UUID.randomUUID();

        TrackedTime trackedTime = new TrackedTime(user, timeID, start, end, timeDesc, duration);

        ArrayList<UUID> userProjectsIDs = new ArrayList<>();
        trackedTime.setProjectIDs(userProjectsIDs);

        trackedTime = entityManager.persistAndFlush(trackedTime);

        this.trackedTime = trackedTime;
    }
    @Before
    public void createSecondTestTimeRecord() {
        Instant start = Instant.parse("2019-12-03T10:20:30.00Z");
        Instant end = Instant.parse("2019-12-03T13:25:30.00Z");
        Duration duration = java.time.Duration.between(start, end);
        String timeDesc = "Second-Test";
        UUID timeID = UUID.randomUUID();

        TrackedTime secondTrackedTime = new TrackedTime(user, timeID, start, end, timeDesc, duration);

        ArrayList<UUID> userProjectIDs = new ArrayList<>();
        secondTrackedTime.setProjectIDs(userProjectIDs);
        secondTrackedTime = entityManager.persistAndFlush(secondTrackedTime);

        this.secondTrackedTime = secondTrackedTime;
    }




    @Before
    public void createTestProjectRecord() {
        UUID projectID = UUID.randomUUID();
        String projectName = "test";
        String projectDesc = "Test-Description";
        String priority = "High";

        UserProject userProject = new UserProject(projectID, projectName, projectDesc, priority, true);
        userProject.setUser(user);

        ArrayList<TrackedTime> trackedTimes = new ArrayList<>();
        trackedTimes.add(trackedTime);

        userProject.setTrackedTimeList(trackedTimes);

        Duration projectDuration =  Duration.parse("PT10M");

        userProject.setProjectTime(projectDuration);

        userProject = entityManager.persistAndFlush(userProject);

        this.userProject = userProject;
    }


    @Test
    public void createProject() {

        assertSame(projectRepository.getByProjectID(userProject.getProjectID()), (userProject));
    }


    @Test
    public void updateProjectDesc() {
        String origDesc = userProject.getProjectDesc();

        userProject.setProjectDesc("Test-Project");

        assertNotSame(projectRepository.getByProjectID(userProject.getProjectID()).getProjectDesc(), (origDesc));


    }

    @Test
    public void countDuration() {

        Duration origTime = userProject.getProjectTime();
        assertSame(projectRepository.getByProjectID(userProject.getProjectID()).getProjectTime(), (origTime));

    }

    @Test
    public void addTime() {

        List<TrackedTime> trackedTimeList = userProject.getTrackedTimeList();

        List<TrackedTime> trackedTimeListNew = new ArrayList<>();

        trackedTimeListNew.add(trackedTime);
        trackedTimeListNew.add(secondTrackedTime);

        userProject.setTrackedTimeList(trackedTimeListNew);

        assertNotSame(projectRepository.getByProjectID(userProject.getProjectID()).getTrackedTimeList(), (trackedTimeList));
    }


    @Test
    public void removeTime() {

        List<TrackedTime> trackedTimeList = userProject.getTrackedTimeList();
        trackedTimeList.remove(trackedTime);

        userProject.setTrackedTimeList(trackedTimeList);

        assertSame(projectRepository.getByProjectID(userProject.getProjectID()).getTrackedTimeList(), (trackedTimeList));



    }

}
