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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@DataJpaTest

public class TrackedTimeTest {

    private User user;
    private TrackedTime trackedTime;
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

        ArrayList<UserProject> userProjects = new ArrayList<>();
        trackedTime.setProjectList(userProjects);

        trackedTime = entityManager.persistAndFlush(trackedTime);

        this.trackedTime = trackedTime;
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



        userProject = entityManager.persistAndFlush(userProject);

        this.userProject = userProject;
    }


    @Test
    public void createTime() {

        assertSame(trackedTimeRepository.getTrackedTimeByTimeID(trackedTime.getTimeID()), (trackedTime));
    }

    @Test
    public void updateTimeDesc() {
        String origDesc = trackedTime.getTimeDesc();

        trackedTime.setTimeDesc("Update");


        assertNotSame(trackedTimeRepository.getTrackedTimeByTimeID(trackedTime.getTimeID()).getTimeDesc(), (origDesc));


    }

    @Test
    public void updateTimeStart() {
        Instant origStart = trackedTime.getStartTime();

        trackedTime.setStartTime(Instant.parse("2019-12-03T10:25:30.00Z"));


        assertNotSame(trackedTimeRepository.getTrackedTimeByTimeID(trackedTime.getTimeID()).getStartTime(), (origStart));


    }

    @Test
    public void updateTimeEnd() {
        Instant origEnd = trackedTime.getEndTime();

        trackedTime.setEndTime(Instant.parse("2019-12-03T11:30:30.00Z"));


        assertNotSame(trackedTimeRepository.getTrackedTimeByTimeID(trackedTime.getTimeID()).getEndTime(), (origEnd));


    }

    @Test
    public void updateTimeDuration() {
        Duration origDuration = trackedTime.getDuration();

        trackedTime.setDuration(java.time.Duration.between(trackedTime.getStartTime(), trackedTime.getEndTime()));

        assertNotSame(trackedTimeRepository.getTrackedTimeByTimeID(trackedTime.getTimeID()).getDuration(), (origDuration));
    }

    @Test
    public void getByUser() {
        assertSame(trackedTimeRepository.getTrackedTimeByTimeID(trackedTime.getTimeID()),
                (trackedTimeRepository.getTrackedTimesByUser(user).get(0)));
    }

    @Test
    public void addProject() {
        UUID projectID = userProject.getProjectID();


        trackedTime.addProject(userProject);


        assertSame(trackedTimeRepository.getTrackedTimeByTimeID(trackedTime.getTimeID()).getProjectList().get(0),
                (projectRepository.getByProjectID(projectID)));
    }

    @Test
    public void removeProject() {

        trackedTime.removeProject(userProject);


        assertTrue(trackedTimeRepository.getTrackedTimeByTimeID(trackedTime.getTimeID()).getProjectList().isEmpty());
    }

}