package ee.desertgun.jttracker.controller;

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

import javax.validation.Valid;
import java.net.InetAddress;
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
  private String frontendPort = "3000";


  public UserAccountController(PasswordEncoder passwordEncoder, UserService userService, EmailService emailService,
                               PasswordTokenValidationService passwordTokenValidationService) {
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
    this.emailService = emailService;
    this.passwordTokenValidationService = passwordTokenValidationService;
  }


  @PostMapping("/user/password/reset")
  public ValidationResponse resetPassword(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
    ValidationResponse response = new ValidationResponse();

    if (userService.userExists(userDTO.getUsername()) && !bindingResult.hasErrors()) {
      response.setValidated(true);
      String token = UUID.randomUUID().toString();
      userService.createPasswordResetTokenForUser(userDTO, token);

      String url = constructResetTokenLink(token, userDTO);
      response.setSuccessMessage(url);
      emailService.sendEmail(userDTO.getUsername(), "Password-Reset", url);


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

    String result = passwordTokenValidationService.validatePasswordResetToken(passwordTokenDTO.getUsername(),passwordTokenDTO.getPasswordResetToken());
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
  public ValidationResponse resetPassword(@RequestBody @Valid UserProfileDTO userProfileDTO){
    ValidationResponse response = new ValidationResponse();
    User user = userService.getUserByUsername(userProfileDTO.getUsername());
    userService.updateUserPassword(user, passwordEncoder.encode(userProfileDTO.getNewPassword()));
    response.setValidated(true);
    response.setSuccessMessage("Password changed");
    emailService.sendEmail(user.getUsername(),"Password-change", "Your Password has been changed");

    return response;
  }

  @PostMapping("/user/password/update")
  public ValidationResponse changeUserPasswordIfOldIsValid(@RequestBody @Valid UserDTO userDTO,
                                                           Authentication authentication) {
    ValidationResponse response = new ValidationResponse();
    User user = userService.getUserByUsername(authentication.getName());
    String userPassword = user.getPassword();

    if (passwordEncoder.matches(userDTO.getOldPassword(), userPassword)) {
      userService.updateUserPassword(user, passwordEncoder.encode(userDTO.getNewPassword()));

      response.setValidated(true);
      response.setSuccessMessage("Password changed");
      emailService.sendEmail(user.getUsername(),"Password-Update", "Your Password has been updated");

    } else {
      response.setValidated(false);
      response.setErrorMessage("Your old-password doesn't match! Please try again");
    }
    return response;
  }


  @PutMapping("/user/update")
  public ValidationResponse updateUserProfile(@RequestBody @Valid UserProfileDTO userProfileDTO) {
    ValidationResponse response = new ValidationResponse();
    User user = userService.getUserByUsername(userProfileDTO.getUsername());
    userService.updateUserProfile(user, userProfileDTO);
    response.setValidated(true);
    emailService.sendEmail(user.getUsername(),"Profile-Update", "Your Profile has been updated");
    return response;
  }


  //TODO: Remember to change to real port when finished and the real server protokol !
  private String constructResetTokenLink(String token, @Valid UserDTO userResetDTO){
    return "http://"+InetAddress.getLoopbackAddress().getHostName() + ":" + frontendPort + "/reset?username=" + userResetDTO.getUsername() + "&token=" + token;
  }


  @GetMapping("/user")
  public User loadUser(Authentication authentication) {
    return userService.getUserByUsername(authentication.getName());
  }
}
