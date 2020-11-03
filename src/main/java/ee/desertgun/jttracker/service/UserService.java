package ee.desertgun.jttracker.service;

import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.dto.UserProfileDTO;
import ee.desertgun.jttracker.dto.UserResetDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User createUser(String username, String displayName, String password, String... roles);

    boolean userExists(String username);

    void createPasswordResetTokenForUser(UserResetDTO userResetDTO, String token);

    User getUserByUsername(String username);

    void updateUserPassword(User user, String password);

    void updateUserProfile(User user, UserProfileDTO userProfileDTO);

    ValidationResponse validateOldUserPassword(String user, String oldPassword);
}
