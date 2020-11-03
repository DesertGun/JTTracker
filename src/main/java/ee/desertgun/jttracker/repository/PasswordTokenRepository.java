package ee.desertgun.jttracker.repository;

import ee.desertgun.jttracker.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    PasswordResetToken findByToken(String token);
}
