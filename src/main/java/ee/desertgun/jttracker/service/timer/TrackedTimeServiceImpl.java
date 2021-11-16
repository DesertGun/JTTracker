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
import java.util.List;
import java.util.UUID;

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
    User user = trackedTimeRepository.getTrackedTimeByTimeID(timeID).getUser();

    List<UserProject> userProjects = projectRepository.getAllByUser(user);
    for (UserProject userProject : userProjects) {
      if (userProject.getTrackedTimeList().contains(trackedTime)) {
        userProject.removeTime(trackedTime);
        userProject.countDuration(userProject.getTrackedTimeList());
        projectRepository.save(userProject);
      }
    }
    trackedTimeRepository.deleteById(timeID);
  }

  @Override
  public void updateTime(UUID timeID, TrackedTimeDTO timer) {
    TrackedTime time = trackedTimeRepository.getTrackedTimeByTimeID(timeID);
    time.setEndTime(timer.getEndTime());
    time.setStartTime(timer.getStartTime());
    time.setTimeDesc(timer.getTimeDesc());
    time.setDuration(timer.getDuration());
    trackedTimeRepository.save(time);

    User user = trackedTimeRepository.getTrackedTimeByTimeID(timeID).getUser();

    List<UserProject> userProjects = projectRepository.getAllByUser(user);
    for (UserProject userProject : userProjects) {
      if (userProject.getTrackedTimeList().contains(time)) {
        userProject.countDuration(userProject.getTrackedTimeList());
        projectRepository.save(userProject);
      }
    }
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
