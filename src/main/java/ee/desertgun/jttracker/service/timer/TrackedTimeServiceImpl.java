package ee.desertgun.jttracker.service.timer;

import ee.desertgun.jttracker.domain.TrackedTime;
import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.domain.UserProject;
import ee.desertgun.jttracker.dto.TrackedTimeDTO;
import ee.desertgun.jttracker.repository.ProjectRepository;
import ee.desertgun.jttracker.repository.TrackedTimeRepository;
import ee.desertgun.jttracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class TrackedTimeServiceImpl implements TrackedTimeService {
    private final UserRepository userRepository;
    private final TrackedTimeRepository trackedTimeRepository;
    private final ProjectRepository projectRepository;

    public TrackedTimeServiceImpl(UserRepository userRepository, TrackedTimeRepository trackedTimeRepository,
                                  ProjectRepository projectRepository) {
        this.trackedTimeRepository = trackedTimeRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public TrackedTime getTimeByID(UUID timeID) {
        return trackedTimeRepository.getTrackedTimeByTimeID(timeID);
    }

    @Override
    public List<TrackedTime> getTimesForUser(String username) {
        User user = userRepository.findByUsername(username);
        return trackedTimeRepository.getTrackedTimesByUser(user);
    }

    @Override
    public void deleteTimeByID(UUID timeID) {
        TrackedTime trackedTime = trackedTimeRepository.getTrackedTimeByTimeID(timeID);
        List<UserProject> userProjects = projectRepository.getAllByUser(trackedTime.getUser());
        for (UserProject userProject : userProjects) {
            List<TrackedTime> trackedTimeList = userProject.getTrackedTimeList();
            if(trackedTimeList.contains(trackedTime)) {
                userProject.setProjectTime(userProject.getProjectTime().minus(trackedTime.getDuration()));
                projectRepository.save(userProject);
                trackedTimeList.remove(trackedTime);
                userProject.setTrackedTimeList(trackedTimeList);
            }
        }
        trackedTimeRepository.deleteById(timeID);
    }

    @Override
    public void updateTime(UUID timeID, TrackedTimeDTO timerNew) {
        TrackedTime timerCurrent = trackedTimeRepository.getTrackedTimeByTimeID(timeID);

        if (timerNew.getDuration() != timerCurrent.getDuration()) {
            List<UserProject> projectsInTrackedTime = projectRepository.getProjectsByProjectIDs(timerCurrent.getProjectIDs());
            for (UserProject userProject : projectsInTrackedTime) {
                Duration currentTimerDuration = timerCurrent.getDuration();
                Duration newTimerDuration = timerNew.getDuration();
                // subtract old duration from project and add the new one
                userProject.setProjectTime(userProject.getProjectTime().minus(currentTimerDuration).plus(newTimerDuration));
                projectRepository.save(userProject);
            }
        }

        timerCurrent.setEndTime(timerNew.getEndTime());
        timerCurrent.setStartTime(timerNew.getStartTime());
        timerCurrent.setTimeDesc(timerNew.getTimeDesc());
        timerCurrent.setDuration(timerNew.getDuration());
        setZoneDateTimeOfTrackedTime(timerCurrent);
    }

    @Override
    public Duration countDuration(Instant startTime, Instant endTime) {
        return Duration.between(startTime, endTime);
    }

    @Override
    public void createTime(TrackedTimeDTO trackedTimeDTO, String username) {
        User user = userRepository.findByUsername(username);
        TrackedTime trackedTime = createNewTrackedTime(trackedTimeDTO, user);
        trackedTime.setDuration(countDuration(trackedTime.getStartTime(), trackedTime.getEndTime()));
        setZoneDateTimeOfTrackedTime(trackedTime);
        trackedTime.setInProject(false);
    }

    private void setZoneDateTimeOfTrackedTime(TrackedTime trackedTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(trackedTime.getEndTime(), ZoneId.systemDefault());
        Calendar calenderDate = GregorianCalendar.from(zonedDateTime);

        trackedTime.setLoggedYear(String.valueOf(calenderDate.get(Calendar.YEAR)));
        trackedTime.setLoggedDayOfYear(calenderDate.get(Calendar.DAY_OF_YEAR));
        trackedTime.setLoggedMonth(calenderDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));

        trackedTimeRepository.save(trackedTime);
    }

    private TrackedTime createNewTrackedTime(TrackedTimeDTO trackedTimeDTO, User user) {
        Instant start = trackedTimeDTO.getStartTime();
        Instant end = trackedTimeDTO.getEndTime();
        UUID id = trackedTimeDTO.getTimeID();
        String desc = trackedTimeDTO.getTimeDesc();
        Duration duration = countDuration(start, end);
        return new TrackedTime(user, id, start, end, desc, duration);
    }
}
