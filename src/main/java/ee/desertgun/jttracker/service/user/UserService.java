package ee.desertgun.jttracker.service.user;

import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.dto.UserProfileDTO;
import java.util.List;

public interface UserService{

    User getOrCreateUser(String keycloakUserId, String username, String email);

    User getUserByKeycloakId(String keycloakUserId) throws Exception;

    User getUserByUsername(String username) throws Exception;

    boolean userExists(String keycloakUserId);

    void updateUserProfile(String keycloakUserId, UserProfileDTO userProfileDTO) throws Exception;

    void addRoleToUser(String keycloakUserId, String role) throws Exception;

    void removeRoleFromUser(String keycloakUserId, String role) throws Exception;

    void deleteUserData(String keycloakUserId) throws Exception;

    List<User> getAllUsers();
}
