package ee.desertgun.jttracker.controller;

import ee.desertgun.jttracker.domain.Mail;
import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.dto.UserProfileDTO;
import ee.desertgun.jttracker.response.ValidationResponse;
import ee.desertgun.jttracker.service.email.EmailService;
import ee.desertgun.jttracker.service.jwt.JwtUserService;
import ee.desertgun.jttracker.service.profilepicture.FileLocationService;
import ee.desertgun.jttracker.service.user.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class UserAccountController {

    private static final String FRONTEND_PORT = "3000";
    private static final String USER_NAME_TEMPLATE = "userName";
    private static final String ACCOUNT_NAME_TEMPLATE = "accountName";
    //private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final EmailService emailService;
    //private final PasswordTokenValidationService passwordTokenValidationService;
    private final FileLocationService fileLocationService;
    private final JwtUserService jwtUserService;

    public UserAccountController(UserService userService, EmailService emailService,
                                 FileLocationService fileLocationService, JwtUserService jwtUserService) {
        this.userService = userService;
        this.emailService = emailService;
        this.fileLocationService = fileLocationService;
        this.jwtUserService = jwtUserService;
    }


    @PutMapping("/user/update")
    public ValidationResponse updateUserProfile(@RequestBody @Valid UserProfileDTO userProfileDTO) throws Exception {
        ValidationResponse response = new ValidationResponse();
        User user = userService.getUserByUsername(userProfileDTO.getUsername());
        userService.updateUserProfile(user.getKeycloakUserId(), userProfileDTO);
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

        if (userService.getUserByUsername(principal.getName()).getProfilePictureID() != null) {
            fileLocationService.deleteImage(userService.getUserByUsername(principal.getName()).getProfilePictureID(),
                    principal.getName());
        }

        fileLocationService.save(profilePicture.getBytes(), profilePicture.getOriginalFilename(), principal.getName());
        validationResponse.setValidated(true);
        validationResponse.setSuccessMessage("Your profile picture was successfully uploaded!");
        return validationResponse;
    }

    @DeleteMapping("/user/picture")
    public ValidationResponse deleteImage() throws Exception {
        JwtUserService.JwtUserInfo userInfo = jwtUserService.getCurrentUser();
        ValidationResponse validationResponse = new ValidationResponse();
        fileLocationService.deleteImage(userService.getUserByUsername(userInfo.getUsername()).getProfilePictureID(),
                userInfo.getAccountName());
        validationResponse.setSuccessMessage("Profile picture was successfully deleted!");
        return validationResponse;
    }

    @DeleteMapping("/user/delete/{username}")
    public ValidationResponse deleteEmployee(@PathVariable(value = "username") String username) throws Exception {
        ValidationResponse response = new ValidationResponse();

        if (!userService.userExists(username)) {
            String error = "Failed to locate your account! Please contact your admin!";
            response.setValidated(false);
            response.setErrorMessage(error);
        } else {
            userService.deleteUserData(userService.getUserByUsername(username).getKeycloakUserId());
            String message = "Account deleted! Hope to see you soon! in 10 seconds your session will be terminated!";
            response.setValidated(true);
            response.setSuccessMessage(message);
        }
        return response;
    }

    @GetMapping(value = "/user/picture/")
    public ResponseEntity<String> downloadImage() throws Exception {
        JwtUserService.JwtUserInfo userInfo = jwtUserService.getCurrentUser();
        User user = userService.getUserByUsername(userInfo.getUsername());
        Path fileSystemResourcePath = Path.of(fileLocationService.find(user.getProfilePictureID()).getPath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(fileSystemResourcePath));
        byte[] encode = Base64.getEncoder().encode(resource.getByteArray());
        String result = new String(encode, StandardCharsets.UTF_8);

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(result);
    }

    @GetMapping("/user")
    public User loadUser() throws Exception {
        JwtUserService.JwtUserInfo userInfo = jwtUserService.getCurrentUser();
        return userService.getUserByUsername(userInfo.getUsername());
    }
}
