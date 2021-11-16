package ee.desertgun.jttracker.repository;

import ee.desertgun.jttracker.domain.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, UUID> {
}
