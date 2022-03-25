package ee.desertgun.jttracker.repository;

import ee.desertgun.jttracker.domain.TrackedTime;
import ee.desertgun.jttracker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrackedTimeRepository extends JpaRepository<TrackedTime, UUID> {
    List<TrackedTime> getTrackedTimesByUser(User user);

    TrackedTime getTrackedTimeByTimeID(UUID timeID);

    void deleteAllByUser(User user);


}
