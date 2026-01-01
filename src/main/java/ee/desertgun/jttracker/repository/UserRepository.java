package ee.desertgun.jttracker.repository;

import ee.desertgun.jttracker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);

    boolean existsByKeycloakUserId(String keycloakUserId);

    boolean existsByUsername(String username);

    // TODO: Usage for future Statistics Refresh
    @Query("SELECT user FROM User user WHERE user.lastLogin > :since")
    List<User> findActiveUsersSince(@Param("since") LocalDateTime since);

    // TODO: Reserved for future Best Practice Sec. Implementation with Keycloak
    @Query("SELECT user FROM User user WHERE :role MEMBER OF user.roles")
    List<User> findByRole(@Param("role") String role);
}
