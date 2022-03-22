package ee.desertgun.jttracker.service.user;

import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.dto.UserDTO;
import ee.desertgun.jttracker.dto.UserProfileDTO;
import ee.desertgun.jttracker.response.ValidationResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface UserService extends UserDetailsService {
    User createUser(String username, String displayName, String password, Boolean securityEnabled, String... roles);

    boolean userExists(String username);

    void createPasswordResetTokenForUser(@Valid UserDTO userDTO, String token);

    User getUserByUsername(String username);

    void updateUserPassword(User user, String password);

    void updateUserProfile(User user, UserProfileDTO userProfileDTO);

    ValidationResponse validateOldUserPassword(String user, String oldPassword);

    void addSecurityQuestions(String username, List<String> securityQuestions, List<String> securityAnswers);

    void disableEnhancedSecurity(UserDTO userDTO);

    void extractEnhancedSecurityDetails(@RequestBody @Valid UserDTO userDTO, PasswordEncoder passwordEncoder, UserService userService);

    void deleteUser(String username);
}
