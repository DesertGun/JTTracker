package ee.desertgun.jttracker.controller;

import ee.desertgun.jttracker.domain.Mail;
import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.dto.PasswordTokenDTO;
import ee.desertgun.jttracker.dto.UserDTO;
import ee.desertgun.jttracker.dto.UserProfileDTO;
import ee.desertgun.jttracker.response.ValidationResponse;
import ee.desertgun.jttracker.service.email.EmailService;
import ee.desertgun.jttracker.service.password.PasswordTokenValidationService;
import ee.desertgun.jttracker.service.profilepicture.FileLocationService;
import ee.desertgun.jttracker.service.user.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Base64;
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
    private final FileLocationService fileLocationService;

    private static final String FRONTEND_PORT = "3000";
    private static final String USER_NAME_TEMPLATE = "userName";
    private static final String ACCOUNT_NAME_TEMPLATE = "accountName";

    public UserAccountController(PasswordEncoder passwordEncoder, UserService userService, EmailService emailService,
                                 PasswordTokenValidationService passwordTokenValidationService, FileLocationService fileLocationService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.emailService = emailService;
        this.passwordTokenValidationService = passwordTokenValidationService;
        this.fileLocationService = fileLocationService;
    }

    @PostMapping("/user/password/reset")
    public ValidationResponse resetPassword(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) throws MessagingException {
        ValidationResponse response = new ValidationResponse();

        if (userService.userExists(userDTO.getUsername()) && !bindingResult.hasErrors()) {
            response.setValidated(true);

            if (Boolean.TRUE.equals(userService.getUserByUsername(userDTO.getUsername()).getSecurityEnabled())) {
                response.setValidated(false);
                final String validationMessage = "Security Authentication required!";
                response.setValidationMessage(validationMessage);
            } else {
                response.setValidated(true);
                response.setSuccessMessage("Reset links was sent!");
                createAndSendPasswordResetToken(userDTO);
            }
        } else if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldErrors().toString();
            response.setValidated(false);
            response.setErrorMessage(error);
        } else {
            String error = "Following E-Mail-Address does not exist! Please re-enter!";
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
            response.setSuccessMessage("Validation successful");
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
        propUpdateReset.put(USER_NAME_TEMPLATE, passwordUpdateResetMail.getMailTo());
        propUpdateReset.put(ACCOUNT_NAME_TEMPLATE, user.getAccountName());
        passwordUpdateResetMail.setProps(propUpdateReset);
        emailService.sendComplexMail(passwordUpdateResetMail, "password_changed");

        return response;
    }

    @PostMapping("/user/password/update")
    public ValidationResponse changeUserPasswordIfOldIsValid(@RequestBody @Valid UserDTO userDTO,
                                                             Principal principal) throws MessagingException {
        ValidationResponse response = new ValidationResponse();
        User user = userService.getUserByUsername(principal.getName());
        String userPassword = user.getPassword();

        if (passwordEncoder.matches(userDTO.getOldPassword(), userPassword)) {
            userService.updateUserPassword(user, passwordEncoder.encode(userDTO.getNewPassword()));

            response.setValidated(true);
            response.setSuccessMessage("Password changed");

            Mail passwordUpdateMail = new Mail();
            passwordUpdateMail.setMailTo(user.getUsername());
            passwordUpdateMail.setSubject("Password-Update");
            Map<String, Object> propPasswordUpdate = new HashMap<>();
            propPasswordUpdate.put(USER_NAME_TEMPLATE, passwordUpdateMail.getMailTo());
            propPasswordUpdate.put(ACCOUNT_NAME_TEMPLATE, user.getAccountName());
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
        propProfileUpdate.put(USER_NAME_TEMPLATE, profileUpdateMail.getMailTo());
        propProfileUpdate.put(ACCOUNT_NAME_TEMPLATE, userProfileDTO.getAccountName());
        profileUpdateMail.setProps(propProfileUpdate);
        emailService.sendComplexMail(profileUpdateMail, "profile_updated");

        return response;
    }

    @PostMapping("/user/picture")
    public ValidationResponse uploadImage(@RequestParam MultipartFile profilePicture, Principal principal) throws Exception {
        ValidationResponse validationResponse = new ValidationResponse();
        fileLocationService.save(profilePicture.getBytes(), profilePicture.getOriginalFilename(), principal.getName());
        validationResponse.setValidated(true);
        validationResponse.setSuccessMessage("Your profile picture was successfully uploaded!");
        return validationResponse;
    }

    @GetMapping(value = "/user/picture/")
    public ResponseEntity<String> downloadImage(Principal principal) throws IOException {
        User user = userService.getUserByUsername(principal.getName());
        Path fileSystemResourcePath = Path.of(fileLocationService.find(user.getProfilePictureID()).getPath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(fileSystemResourcePath));
        byte[] encode = Base64.getEncoder().encode(resource.getByteArray());
        String result = new String(encode, StandardCharsets.UTF_8);

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(result);
    }

    private void createAndSendPasswordResetToken(UserDTO userDTO) throws MessagingException {
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(userDTO, token);

        String url = "http://" + InetAddress.getLoopbackAddress().getHostName() + ":" + FRONTEND_PORT + "/reset?username=" + userDTO.getUsername() + "&token=" + token;

        Mail passwordResetMail = new Mail();
        passwordResetMail.setMailTo(userDTO.getUsername());
        passwordResetMail.setSubject("Password-Reset");
        Map<String, Object> propPasswordReset = new HashMap<>();
        propPasswordReset.put("passwordResetLink", url);
        passwordResetMail.setProps(propPasswordReset);

        emailService.sendComplexMail(passwordResetMail, "password_reset");
    }

    @GetMapping("/user")
    public User loadUser(Principal principal) {
        return userService.getUserByUsername(principal.getName());
    }

    @GetMapping("/security/{username}")
    public Map<String, String> getSecurityQuestions(@PathVariable String username) {

        HashMap<String, String> securityQuestions = new HashMap<>();
        final User user = userService.getUserByUsername(username);
        String securityQuestion1 = user.getSecurityQuestion1();
        String securityQuestion2 = user.getSecurityQuestion2();
        String securityQuestion3 = user.getSecurityQuestion3();

        securityQuestions.put("securityQuestion1", securityQuestion1);
        securityQuestions.put("securityQuestion2", securityQuestion2);
        securityQuestions.put("securityQuestion3", securityQuestion3);

        return securityQuestions;
    }

    @PostMapping("/security")
    public ValidationResponse validateSecurityQuestions(@RequestBody @Valid UserDTO userDTO) throws MessagingException {
        ValidationResponse validationResponse = new ValidationResponse();

        final User user = userService.getUserByUsername(userDTO.getUsername());

        if ((passwordEncoder.matches(userDTO.getSecurityAnswer1(), user.getSecurityAnswer1()))
                && (passwordEncoder.matches(userDTO.getSecurityAnswer2(), user.getSecurityAnswer2()))
                && (passwordEncoder.matches(userDTO.getSecurityAnswer3(), user.getSecurityAnswer3()))) {

            validationResponse.setValidated(true);
            validationResponse.setSuccessMessage("Correct answers!");
            createAndSendPasswordResetToken(userDTO);
        } else {
            validationResponse.setValidated(false);
            validationResponse.setErrorMessage("Wrong answers!");
        }
        return validationResponse;
    }

    // TODO: Add E-Mail Notification for removal of security-questions
    @PatchMapping("/security")
    public ValidationResponse disableSecurityQuestions(@RequestBody @Valid UserDTO userDTO) {
        ValidationResponse response = new ValidationResponse();
        userService.disableEnhancedSecurity(userDTO);
        response.setSuccessMessage("Removed securityQuestions!");
        return response;
    }
}
