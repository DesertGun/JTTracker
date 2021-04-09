package ee.desertgun.jttracker.repository;


import ee.desertgun.jttracker.domain.TrackedTime;
import ee.desertgun.jttracker.domain.User;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest

public class TimeJPATest {

    private User user;
    private TrackedTime trackedTime;

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TrackedTimeRepository trackedTimeRepository;

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
        trackedTime = entityManager.persistAndFlush(trackedTime);

        this.trackedTime = trackedTime;
    }


    @Test
    public void createTime() {

        assertSame(trackedTimeRepository.getTrackedTimeByTimeID(trackedTime.getTimeID()), (trackedTime));
    }

    @Test
    public void updateTime() {
        String origDesc = trackedTime.getTimeDesc();

        trackedTime.setTimeDesc("Update");

        trackedTimeRepository.save(trackedTime);
        assertNotSame(trackedTimeRepository.getTrackedTimeByTimeID(trackedTime.getTimeID()).getTimeDesc(), (origDesc));


    }
}