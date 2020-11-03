package ee.desertgun.jttracker.controller;

import ee.desertgun.jttracker.dto.UserDTO;
import ee.desertgun.jttracker.service.EmailService;
import ee.desertgun.jttracker.service.UserService;
import ee.desertgun.jttracker.service.ValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@CrossOrigin
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
public class UserRegistrationController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final EmailService emailService;
    Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);

    public UserRegistrationController(PasswordEncoder passwordEncoder, UserService userService, EmailService emailService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.emailService = emailService;
    }


    @PostMapping("/register")
    public ValidationResponse registerUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        ValidationResponse response = new ValidationResponse();
        if (bindingResult.hasErrors()) {
          String error = bindingResult.getFieldErrors().toString();
          response.setValidated(false);
          response.setErrorMessage(error);
        } else if (userService.userExists(userDTO.getUsername())){
          String error = "You are already registered! Please log into your account!";
          response.setValidated(false);
          response.setErrorMessage(error);
        } else {
        createUserAccount(userDTO);
        response.setValidated(true);
        response.setSuccessMessage("Thank you for the registration!");
        emailService.sendEmail(userDTO.getUsername(), "Registration", "Thanks for Registration");
      }
        return response;
    }


    //TODO: Exception for failed User-creation
    private void createUserAccount(UserDTO userDTO) {

        String displayName = userDTO.getAccountName();
        String username = userDTO.getUsername();
        String password = passwordEncoder.encode(userDTO.getPassword());
        try {
            userService.createUser(username, displayName, password, "ROLE_USER");
        } catch (Exception e) {
            logger.warn("Not possible to create new User");
        }
    }
}
