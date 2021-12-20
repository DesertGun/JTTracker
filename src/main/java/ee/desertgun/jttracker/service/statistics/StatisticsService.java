package ee.desertgun.jttracker.service.statistics;

import ee.desertgun.jttracker.domain.TrackedTime;
import ee.desertgun.jttracker.domain.UserProject;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    Map<String, Object> createUserStatistics(List<TrackedTime> trackedTimeList, List<UserProject> userProjectList);
}
