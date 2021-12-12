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
import java.util.*;

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
        int timersInProjectsTotal = 0;
        int timersNotInProjectsTotal = 0;

        List<TrackedTime> trackedTimeList = trackedTimeService.getTimesForUser(principal.getName());
        List<UserProject> userProjects = userProjectService.getUserProjects(principal.getName());

        int numberOfTimers = trackedTimeList.size();
        int numberOfProjects = userProjects.size();
        Duration avgTimeTracked;

        Duration userProjectDuration;
        Duration trackedTimeDuration;
        List<Duration> durationListProject = new ArrayList<>();
        List<Duration> durationListTrackedTime = new ArrayList<>();


        if (!trackedTimeList.isEmpty()) {

            for (TrackedTime trackedTime : trackedTimeList) {
                durationListTrackedTime.add((trackedTime.getDuration()));

                // TODO: Find and fix logical error with timer in a project availability
                if (trackedTime.getProjectList().isEmpty()) {
                    timersNotInProjectsTotal++;
                } else {
                    timersInProjectsTotal++;
                }
            }

            trackedTimeDuration = durationListTrackedTime.stream().reduce(Duration.ZERO, Duration::plus);
            avgTimeTracked = trackedTimeDuration.dividedBy(trackedTimeList.size());

            Duration maxDuration = Collections.max(durationListTrackedTime);
            Duration minDuration = Collections.min(durationListTrackedTime);

            statistics.put("totalTimeTracked", trackedTimeDuration);
            statistics.put("avgTimeTracked", avgTimeTracked);
            statistics.put("maxDuration", maxDuration);
            statistics.put("minDuration", minDuration);
            statistics.put("timersInProjectsTotal", timersInProjectsTotal);
            statistics.put("timersNotInProjectsTotal", timersNotInProjectsTotal);

        }

        if (!userProjects.isEmpty()) {
            for (UserProject userProject : userProjects) {
                if (userProject.getProjectTime() != null) {
                    durationListProject.add((userProject.getProjectTime()));
                }
            }

            userProjectDuration = durationListProject.stream().reduce(Duration.ZERO, Duration::plus);
            statistics.put("totalTimeTrackedInProjects", userProjectDuration);
        }

        statistics.put("numberOfTimers", numberOfTimers);
        statistics.put("numberOfProjects", numberOfProjects);
        logger.info(statistics.values().toString());

        return statistics;
    }
}
