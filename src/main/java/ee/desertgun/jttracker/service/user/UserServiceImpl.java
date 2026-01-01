package ee.desertgun.jttracker.service.user;

import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.dto.UserProfileDTO;
import ee.desertgun.jttracker.repository.ProjectRepository;
import ee.desertgun.jttracker.repository.TrackedTimeRepository;
import ee.desertgun.jttracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final TrackedTimeRepository trackedTimeRepository;
    private final ProjectRepository projectRepository;

    public UserServiceImpl(UserRepository userRepository, TrackedTimeRepository trackedTimeRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.trackedTimeRepository = trackedTimeRepository;
        this.projectRepository = projectRepository;
    }

    // Since using Keycloak, now I have to sync user via Service
    @Override
    @Transactional
    public User getOrCreateUser(String keycloakUserId, String username, String email) {
        return userRepository.findById(keycloakUserId)
                .map(existingUser -> {
                    // Update username if changed in Keycloak
                    if (!existingUser.getUsername().equals(username)) {
                        existingUser.setUsername(username);
                        logger.info("Updated username for user {}: {} -> {}",
                                keycloakUserId, existingUser.getUsername(), username);
                    }
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setKeycloakUserId(keycloakUserId);
                    newUser.setUsername(username);
                    newUser.setAccountName(username); // Default display name
                    newUser.setRoles(new ArrayList<>());

                    User saved = userRepository.save(newUser);
                    logger.info("Created new user profile for Keycloak user: {}", keycloakUserId);
                    return saved;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByKeycloakId(String keycloakUserId) throws Exception {
        return userRepository.findById(keycloakUserId)
                .orElseThrow(() -> new Exception(
                        "User not found with Keycloak ID: " + keycloakUserId
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(String keycloakUserId) {
        return userRepository.existsByKeycloakUserId(keycloakUserId);
    }

    @Override
    @Transactional
    public void updateUserProfile(String keycloakUserId, UserProfileDTO userProfileDTO) throws Exception {
        User user = getUserByKeycloakId(keycloakUserId);

        if (userProfileDTO.getAccountName() != null) {
            user.setAccountName(userProfileDTO.getAccountName());
        }

        userRepository.save(user);
        logger.info("Updated profile for user: {}", keycloakUserId);
    }

    @Override
    @Transactional
    public void addRoleToUser(String keycloakUserId, String role) throws Exception {
        User user = getUserByKeycloakId(keycloakUserId);

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
            logger.info("Added role {} to user {}", role, keycloakUserId);
        }
    }

    @Override
    @Transactional
    public void removeRoleFromUser(String keycloakUserId, String role) throws Exception {
        User user = getUserByKeycloakId(keycloakUserId);

        if (user.getRoles() != null && user.getRoles().contains(role)) {
            user.getRoles().remove(role);
            userRepository.save(user);
            logger.info("Removed role {} from user {}", role, keycloakUserId);
        }
    }

    /**
     * TODO: Find a way to sync User Removal from keycloak
     */
    @Override
    @Transactional
    public void deleteUserData(String keycloakUserId) throws Exception {
        User user = getUserByKeycloakId(keycloakUserId);

        logger.info("Starting deletion of all data for user: {}", keycloakUserId);

        // Delete all user's projects
        projectRepository.deleteAllByUser(user);
        logger.debug("Deleted projects for user: {}", keycloakUserId);

        // Delete all user's tracked times
        trackedTimeRepository.deleteAllByUser(user);
        logger.debug("Deleted tracked times for user: {}", keycloakUserId);

        // Delete user profile
        userRepository.delete(user);
        logger.info("Completed deletion of all data for user: {}", keycloakUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public long getUserCount() {
        return userRepository.count();
    }
}
