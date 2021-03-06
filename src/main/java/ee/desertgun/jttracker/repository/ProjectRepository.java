package ee.desertgun.jttracker.repository;

import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.domain.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<UserProject, UUID> {

    List<UserProject> getAllByUser(User user);

    UserProject getByProjectID(UUID projectID);

}
