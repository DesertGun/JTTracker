package ee.desertgun.jttracker.service.statistics;

import ee.desertgun.jttracker.domain.TrackedTime;
import ee.desertgun.jttracker.domain.UserProject;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final Integer[] PRODUCTIVITY_LEVEL = {1, 2, 3, 4, 5};

    @Override
    public Map<String, Object> createUserStatistics(List<TrackedTime> trackedTimeList, List<UserProject> userProjectList) {

        Map<String, Object> timerStatistics = getTimerStatistics(trackedTimeList, userProjectList);
        Map<String, Object> projectStatistics = getProjectStatistics(userProjectList);

        timerStatistics.putAll(projectStatistics);
        return timerStatistics;
    }

    private Map<String, Object> getProjectStatistics(List<UserProject> userProjectList) {

        Map<String, Object> projectStatistics = new HashMap<>();
        int numberOfProjects = userProjectList.size();
        Duration userProjectDuration;
        List<Duration> durationListProject = new ArrayList<>();

        if (!userProjectList.isEmpty()) {
            for (UserProject userProject : userProjectList) {
                if (userProject.getProjectTime() != null) {
                    durationListProject.add((userProject.getProjectTime()));
                }
            }
            userProjectDuration = durationListProject.stream().reduce(Duration.ZERO, Duration::plus);
            projectStatistics.put("totalTimeTrackedInProjects", userProjectDuration);
        }
        projectStatistics.put("numberOfProjects", numberOfProjects);
        return projectStatistics;
    }

    private Map<String, Object> getTimerStatistics(List<TrackedTime> trackedTimeList, List<UserProject> userProjectList) {
        Map<String, Object> timerStatistics = new HashMap<>();

        int timersInProjectsTotal = 0;
        int timersNotInProjectsTotal = 0;
        Duration avgTimeTracked;

        Duration trackedTimeDuration;

        List<Duration> durationListTrackedTime = new ArrayList<>();

        List<String> years = new ArrayList<>();
        List<String> months = new ArrayList<>();

        int numberOfTimers = trackedTimeList.size();

        if (!trackedTimeList.isEmpty()) {
            for (TrackedTime trackedTime : trackedTimeList) {
                durationListTrackedTime.add((trackedTime.getDuration()));

                String loggedYear = trackedTime.getLoggedYear();

                String loggedMonth = trackedTime.getLoggedMonth();

                years.add(loggedYear);
                months.add(loggedMonth);

                if (trackedTime.getProjectList().isEmpty()) {
                    timersNotInProjectsTotal += 1;
                } else {
                    timersInProjectsTotal += 1;
                }
            }

            int productivityLevel = getProductivityLevel(trackedTimeList);

            trackedTimeDuration = durationListTrackedTime.stream().reduce(Duration.ZERO, Duration::plus);
            avgTimeTracked = trackedTimeDuration.dividedBy(trackedTimeList.size());

            Duration maxDuration = Collections.max(durationListTrackedTime);
            Duration minDuration = Collections.min(durationListTrackedTime);

            String mostProductiveMonth = getMostOccurringObject(months);
            String mostProductiveYear = getMostOccurringObject(years);

            timerStatistics.put("productivityLevel", productivityLevel);
            timerStatistics.put("totalTimeTracked", trackedTimeDuration);
            timerStatistics.put("avgTimeTracked", avgTimeTracked);
            timerStatistics.put("maxDuration", maxDuration);
            timerStatistics.put("minDuration", minDuration);
            timerStatistics.put("timersInProjectsTotal", timersInProjectsTotal);
            timerStatistics.put("timersNotInProjectsTotal", timersNotInProjectsTotal);
            timerStatistics.put("mostProductiveYear", mostProductiveYear);
            timerStatistics.put("mostProductiveMonth", mostProductiveMonth);
            timerStatistics.put("numberOfTimers", numberOfTimers);
        }
        return timerStatistics;
    }

    private String getMostOccurringObject(List<String> object) {
        String mostOccurringObject = "";
        Optional<Map.Entry<String, Long>> optional = object.stream()
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());
        if (optional.isPresent()) {
            mostOccurringObject = optional.get().getKey();
        }
        return mostOccurringObject;
    }

    private Integer getProductivityLevel(List<TrackedTime> trackedTimeList) {

        Map<String, List<Integer>> yearMap = new HashMap<>();

        for (TrackedTime trackedTime : trackedTimeList) {

            List<Integer> loggedDaysOfYear = new ArrayList<>();
            String loggedYear = trackedTime.getLoggedYear();
            int loggedDayOfYear = trackedTime.getLoggedDayOfYear();

            if (yearMap.containsKey(loggedYear)) {
                yearMap.get(loggedYear).add(loggedDayOfYear);
            } else {
                loggedDaysOfYear.add(loggedDayOfYear);
                yearMap.put(loggedYear, loggedDaysOfYear);
            }
        }

        int maxDistanceBetweenLastLoggedDay = 365;
        int maxDayFromCurrentYear;

        for (Map.Entry<String, List<Integer>> entry : yearMap.entrySet()) {
            List<Integer> values = entry.getValue();

            Instant instant = Instant.now();

            int dayCurrent = GregorianCalendar.from(ZonedDateTime.ofInstant(instant,
                    ZoneId.systemDefault())).get(Calendar.DAY_OF_YEAR);
            String yearCurrent = String.valueOf(GregorianCalendar.from(ZonedDateTime.ofInstant(instant,
                    ZoneId.systemDefault())).get(Calendar.YEAR));

            if (entry.getKey().equals(yearCurrent)) {
                maxDayFromCurrentYear = Collections.max(values);
                maxDistanceBetweenLastLoggedDay = dayCurrent - maxDayFromCurrentYear;
            }
        }

        if (maxDistanceBetweenLastLoggedDay < 1) {
            return PRODUCTIVITY_LEVEL[4];
        } else if (maxDistanceBetweenLastLoggedDay <= 2) {
            return PRODUCTIVITY_LEVEL[3];
        } else if (maxDistanceBetweenLastLoggedDay <= 4) {
            return PRODUCTIVITY_LEVEL[2];
        } else if (maxDistanceBetweenLastLoggedDay <= 7) {
            return PRODUCTIVITY_LEVEL[1];
        } else {
            return PRODUCTIVITY_LEVEL[0];
        }
    }
}
