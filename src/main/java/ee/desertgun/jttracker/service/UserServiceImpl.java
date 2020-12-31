package ee.desertgun.jttracker.service;

import ee.desertgun.jttracker.config.UserProfileGravatarHash;
import ee.desertgun.jttracker.domain.PasswordResetToken;
import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.dto.UserDTO;
import ee.desertgun.jttracker.dto.UserProfileDTO;
import ee.desertgun.jttracker.repository.PasswordTokenRepository;
import ee.desertgun.jttracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordTokenRepository passwordTokenRepository;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final PasswordTokenRepository passwordTokenRepository) {
        this.userRepository = userRepository;
        this.passwordTokenRepository = passwordTokenRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User name " + username + " not found."));
    }

    @Override
    public User createUser(String username, String displayName, String password, String... roles) {
        String hash = UserProfileGravatarHash.md5Hex(username);
        final User user = new User(username, displayName, password, hash);
        for (final String role : roles) {
            user.addRole(role);
        }

        return userRepository.save(user);
    }

    @Override
    public boolean userExists(String username) {
      return userRepository.existsUserByUsername(username);
    }

    @Override
    public void createPasswordResetTokenForUser(@Valid UserDTO userDTO, String token) {
        User user = userRepository.findByUsername(userDTO.getUsername());
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void updateUserPassword(User user, String password) {
        user.setPassword(password);
        userRepository.save(user);
    }

    @Override
    public void updateUserProfile(User user, UserProfileDTO userProfileDTO) {
        user.setAccountName(userProfileDTO.getAccountName());
        userRepository.save(user);
    }

    @Override
    public ValidationResponse validateOldUserPassword(String userPassword, String oldPassword) {
        ValidationResponse validationResponse = new ValidationResponse();
        if (userPassword.equals(oldPassword)) {
            validationResponse.setValidated(true);
            return validationResponse;
        } else {
            validationResponse.setValidated(false);
        }
        return validationResponse;
    }
}
