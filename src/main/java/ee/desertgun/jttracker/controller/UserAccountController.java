package ee.desertgun.jttracker.controller;

import ee.desertgun.jttracker.domain.Mail;
import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.dto.PasswordTokenDTO;
import ee.desertgun.jttracker.dto.UserDTO;
import ee.desertgun.jttracker.dto.UserProfileDTO;
import ee.desertgun.jttracker.service.EmailService;
import ee.desertgun.jttracker.service.PasswordTokenValidationService;
import ee.desertgun.jttracker.service.UserService;
import ee.desertgun.jttracker.service.ValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@CrossOrigin
public class UserAccountController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordTokenValidationService passwordTokenValidationService;

    @Value("${server.port}")
    private String port;

    //TODO: Only for testing!!!
    private static final String FRONTEND_PORT = "3000";


    public UserAccountController(PasswordEncoder passwordEncoder, UserService userService, EmailService emailService,
                                 PasswordTokenValidationService passwordTokenValidationService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.emailService = emailService;
        this.passwordTokenValidationService = passwordTokenValidationService;
    }


    @PostMapping("/user/password/reset")
    public ValidationResponse resetPassword(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) throws MessagingException {
        ValidationResponse response = new ValidationResponse();

        if (userService.userExists(userDTO.getUsername()) && !bindingResult.hasErrors()) {
            response.setValidated(true);
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(userDTO, token);

            String url = constructResetTokenLink(token, userDTO);
            response.setSuccessMessage(url);

            Mail passwordResetMail = new Mail();
            passwordResetMail.setMailTo(userDTO.getUsername());
            passwordResetMail.setSubject("Password-Reset");
            Map<String, Object> propPasswordReset = new HashMap<>();
            propPasswordReset.put("passwordResetLink", url);
            passwordResetMail.setProps(propPasswordReset);
            emailService.sendComplexMail(passwordResetMail, "password_reset");

        } else if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldErrors().toString();
            response.setValidated(false);
            response.setErrorMessage(error);
        } else {
            String error = "Following E-Mail-Adress does not exist! Please re-enter!";
            response.setValidated(false);
            response.setErrorMessage(error);
        }

        return response;
    }

    @PostMapping("/user/password/reset/validate")
    public ValidationResponse checkToken(@RequestBody @Valid PasswordTokenDTO passwordTokenDTO) {
        ValidationResponse response = new ValidationResponse();

        String result = passwordTokenValidationService.validatePasswordResetToken(passwordTokenDTO.getUsername(), passwordTokenDTO.getPasswordResetToken());
        if (result == null) {
            response.setValidated(true);
            response.setSuccessMessage("Validation successfull");
        } else if (result.equals("invalidToken") || result.equals("expired")) {
            response.setValidated(false);
            response.setErrorMessage("Token is invalid");
        }
        return response;
    }


    @PostMapping("/user/password/reset/change")
    public ValidationResponse resetPassword(@RequestBody @Valid UserProfileDTO userProfileDTO) throws MessagingException {
        ValidationResponse response = new ValidationResponse();
        User user = userService.getUserByUsername(userProfileDTO.getUsername());
        userService.updateUserPassword(user, passwordEncoder.encode(userProfileDTO.getNewPassword()));
        response.setValidated(true);
        response.setSuccessMessage("Password changed");

        Mail passwordUpdateResetMail = new Mail();
        passwordUpdateResetMail.setMailTo(user.getUsername());
        passwordUpdateResetMail.setSubject("Password-Reset completed");
        Map<String, Object> propUpdateReset = new HashMap<>();
        propUpdateReset.put("userName", passwordUpdateResetMail.getMailTo());
        propUpdateReset.put("displayName", user.getAccountName());
        passwordUpdateResetMail.setProps(propUpdateReset);
        emailService.sendComplexMail(passwordUpdateResetMail, "password_changed");

        return response;
    }

    @PostMapping("/user/password/update")
    public ValidationResponse changeUserPasswordIfOldIsValid(@RequestBody @Valid UserDTO userDTO,
                                                             Authentication authentication) throws MessagingException {
        ValidationResponse response = new ValidationResponse();
        User user = userService.getUserByUsername(authentication.getName());
        String userPassword = user.getPassword();

        if (passwordEncoder.matches(userDTO.getOldPassword(), userPassword)) {
            userService.updateUserPassword(user, passwordEncoder.encode(userDTO.getNewPassword()));

            response.setValidated(true);
            response.setSuccessMessage("Password changed");

            Mail passwordUpdateMail = new Mail();
            passwordUpdateMail.setMailTo(user.getUsername());
            passwordUpdateMail.setSubject("Password-Update");
            Map<String, Object> propPasswordUpdate = new HashMap<>();
            propPasswordUpdate.put("userName", passwordUpdateMail.getMailTo());
            propPasswordUpdate.put("displayName", user.getAccountName());
            passwordUpdateMail.setProps(propPasswordUpdate);
            emailService.sendComplexMail(passwordUpdateMail, "password_changed");

        } else {
            response.setValidated(false);
            response.setErrorMessage("Your old-password doesn't match! Please try again");
        }
        return response;
    }


    @PutMapping("/user/update")
    public ValidationResponse updateUserProfile(@RequestBody @Valid UserProfileDTO userProfileDTO) throws MessagingException {
        ValidationResponse response = new ValidationResponse();
        User user = userService.getUserByUsername(userProfileDTO.getUsername());
        userService.updateUserProfile(user, userProfileDTO);
        response.setValidated(true);

        Mail profileUpdateMail = new Mail();
        profileUpdateMail.setMailTo(userProfileDTO.getUsername());
        profileUpdateMail.setSubject("Profile-Update");
        Map<String, Object> propProfileUpdate = new HashMap<>();
        propProfileUpdate.put("userName", profileUpdateMail.getMailTo());
        propProfileUpdate.put("displayName", userProfileDTO.getAccountName());
        profileUpdateMail.setProps(propProfileUpdate);
        emailService.sendComplexMail(profileUpdateMail, "profile_updated");

        return response;
    }


    //TODO: Remember to change to real port when finished and the real server protocol !
    private String constructResetTokenLink(String token, @Valid UserDTO userResetDTO) {
        return "http://" + InetAddress.getLoopbackAddress().getHostName() + ":" + FRONTEND_PORT + "/reset?username=" + userResetDTO.getUsername() + "&token=" + token;
    }


    @GetMapping("/user")
    public User loadUser(Authentication authentication) {
        return userService.getUserByUsername(authentication.getName());
    }
}
