package ee.desertgun.jttracker.service.timer;

import ee.desertgun.jttracker.domain.TrackedTime;
import ee.desertgun.jttracker.dto.TrackedTimeDTO;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TrackedTimeService {

    TrackedTime getTimeByID(UUID timeID);

    List<TrackedTime> getTimesForUser(String username);

    void deleteTimeByID(UUID timeID);

    void updateTime(UUID timeID, TrackedTimeDTO trackedTimeDTO);

    Duration countDuration(Instant startTime, Instant endTime);

    void createTime(TrackedTimeDTO trackedTimeDTO, String username);
}
