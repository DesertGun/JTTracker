package ee.desertgun.jttracker.repository;

import ee.desertgun.jttracker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);

    Optional<User> findOneByUsername(String username);

    Boolean existsUserByUsername(String username);
}
