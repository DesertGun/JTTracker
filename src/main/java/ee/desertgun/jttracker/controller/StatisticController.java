package ee.desertgun.jttracker.controller;

import ee.desertgun.jttracker.domain.TrackedTime;
import ee.desertgun.jttracker.domain.UserProject;
import ee.desertgun.jttracker.service.timer.TrackedTimeService;
import ee.desertgun.jttracker.service.userproject.UserProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class StatisticController {

    private final TrackedTimeService trackedTimeService;
    private final UserProjectService userProjectService;

    public StatisticController(final TrackedTimeService trackedTimeService, final UserProjectService userProjectService) {
        this.trackedTimeService = trackedTimeService;
        this.userProjectService = userProjectService;
    }

    Logger logger = LoggerFactory.getLogger(StatisticController.class);

    @GetMapping("/statistics")
    public Map<String, Object> getUserTimerStatistics(Principal principal) {
        Map<String, Object> statistics = new HashMap<>();

        List<TrackedTime> trackedTimeList = trackedTimeService.getTimesForUser(principal.getName());
        List<UserProject> userProjects = userProjectService.getUserProjects(principal.getName());

        int numberOfTimers = trackedTimeList.size();
        int numberOfProjects = userProjects.size();

        Duration userProjectDuration;
        Duration trackedTimeDuration;
        List<Duration> durationListProject = new ArrayList<>();
        List<Duration> durationListTrackedTime = new ArrayList<>();

        for (TrackedTime trackedTime : trackedTimeList) {
            durationListProject.add((trackedTime.getDuration()));
        }
        userProjectDuration = durationListProject.stream().reduce(Duration.ZERO, Duration::plus);

        for (UserProject userProject : userProjects) {
            durationListTrackedTime.add((userProject.getProjectTime()));
        }
        trackedTimeDuration = durationListTrackedTime.stream().reduce(Duration.ZERO, Duration::plus);

        statistics.put("numberOfTimers", numberOfTimers);
        statistics.put("numberOfProjects", numberOfProjects);
        statistics.put("totalTimeTracked", trackedTimeDuration);
        statistics.put("totalTimeTrackedInProject", userProjectDuration);
        logger.info(statistics.values().toString());

        return statistics;
    }
}
