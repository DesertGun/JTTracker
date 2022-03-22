package ee.desertgun.jttracker.service.user;

import ee.desertgun.jttracker.config.UserProfileGravatarHash;
import ee.desertgun.jttracker.domain.PasswordResetToken;
import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.dto.UserDTO;
import ee.desertgun.jttracker.dto.UserProfileDTO;
import ee.desertgun.jttracker.repository.PasswordTokenRepository;
import ee.desertgun.jttracker.repository.UserRepository;
import ee.desertgun.jttracker.response.ValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
    public User createUser(String username, String displayName, String password, Boolean securityEnabled, String... roles) {
        String hash = UserProfileGravatarHash.md5Hex(username);
        final User user = new User(username, displayName, password, hash, securityEnabled);
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

    @Override
    public void addSecurityQuestions(String username, List<String> securityQuestions, List<String> securityAnswers) {
        User user = userRepository.findByUsername(username);

        user.setSecurityQuestion1(securityQuestions.get(0));
        user.setSecurityQuestion2(securityQuestions.get(1));
        user.setSecurityQuestion3(securityQuestions.get(2));

        user.setSecurityAnswer1(securityAnswers.get(0));
        user.setSecurityAnswer2(securityAnswers.get(1));
        user.setSecurityAnswer3(securityAnswers.get(2));

        user.setSecurityEnabled(true);

        userRepository.save(user);
    }

    @Override
    public void disableEnhancedSecurity(UserDTO userDTO) {
        final User user = userRepository.getById(userDTO.getUsername());
        user.setSecurityEnabled(false);
        user.setSecurityQuestion1(null);
        user.setSecurityQuestion2(null);
        user.setSecurityQuestion3(null);
        user.setSecurityAnswer1(null);
        user.setSecurityAnswer2(null);
        user.setSecurityAnswer3(null);
        userRepository.save(user);
    }

    @Override
    public void extractEnhancedSecurityDetails(@RequestBody @Valid UserDTO userDTO, PasswordEncoder passwordEncoder, UserService userService) {
        List<String> securityQuestions = new ArrayList<>();
        securityQuestions.add(userDTO.getSecurityQuestion1());
        securityQuestions.add(userDTO.getSecurityQuestion2());
        securityQuestions.add(userDTO.getSecurityQuestion3());

        List<String> securityAnswers = new ArrayList<>();
        securityAnswers.add(passwordEncoder.encode(userDTO.getSecurityAnswer1()));
        securityAnswers.add(passwordEncoder.encode(userDTO.getSecurityAnswer2()));
        securityAnswers.add(passwordEncoder.encode(userDTO.getSecurityAnswer3()));

        userService.addSecurityQuestions(userDTO.getUsername(), securityQuestions, securityAnswers);
    }
    @Override
    public void deleteUser(String username) {
        User user = userRepository.getById(username);
        userRepository.delete(user);
    }
}
