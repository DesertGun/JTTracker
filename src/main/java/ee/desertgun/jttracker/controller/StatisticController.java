package ee.desertgun.jttracker.controller;

import ee.desertgun.jttracker.domain.TrackedTime;
import ee.desertgun.jttracker.domain.UserProject;
import ee.desertgun.jttracker.service.statistics.StatisticsService;
import ee.desertgun.jttracker.service.timer.TrackedTimeService;
import ee.desertgun.jttracker.service.userproject.UserProjectService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class StatisticController {

    private final TrackedTimeService trackedTimeService;
    private final UserProjectService userProjectService;
    private final StatisticsService statisticsService;

    public StatisticController(final TrackedTimeService trackedTimeService, final UserProjectService userProjectService,
                               final StatisticsService statisticsService) {
        this.trackedTimeService = trackedTimeService;
        this.userProjectService = userProjectService;
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics")
    public Map<String, Object> getUserTimerStatistics(Principal principal) {

        List<TrackedTime> trackedTimeList = trackedTimeService.getTimesForUser(principal.getName());
        List<UserProject> userProjectList = userProjectService.getUserProjects(principal.getName());

        return statisticsService.createUserStatistics(trackedTimeList, userProjectList);
    }
}
