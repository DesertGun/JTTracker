package ee.desertgun.jttracker.controller;

import ee.desertgun.jttracker.domain.Mail;
import ee.desertgun.jttracker.dto.UserDTO;
import ee.desertgun.jttracker.response.ValidationResponse;
import ee.desertgun.jttracker.service.email.EmailService;
import ee.desertgun.jttracker.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
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
    public ValidationResponse registerUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) throws MessagingException {
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

            Mail registrationMail = new Mail();
            registrationMail.setMailTo(userDTO.getUsername());
            registrationMail.setSubject("Registration");
            Map<String, Object> propRegistration = new HashMap<>();
            propRegistration.put("userName", registrationMail.getMailTo());
            propRegistration.put("displayName", userDTO.getAccountName());
            registrationMail.setProps(propRegistration);
            emailService.sendComplexMail(registrationMail, "register");
      }
        return response;
    }


    private void createUserAccount(UserDTO userDTO) {

        // TODO: Add variable that checks whether user wants to enable security_questions
        // TODO: Refactor rename variable to check whether user has security_questions enabled
        String displayName = userDTO.getAccountName();
        String username = userDTO.getUsername();
        String password = passwordEncoder.encode(userDTO.getPassword());
        try {
            if (userDTO.getSecurityQuestions() != null) {
                userService.createUser(username, displayName, password, true,"ROLE_USER");
                List<String> securityQuestions = new ArrayList<>();
                securityQuestions.add(userDTO.getSecurityQuestion_1());
                securityQuestions.add(userDTO.getSecurityQuestion_2());
                securityQuestions.add(userDTO.getSecurityQuestion_3());

                List<String> securityAnswers = new ArrayList<>();
                securityAnswers.add(passwordEncoder.encode(userDTO.getSecurityAnswer_1()));
                securityAnswers.add(passwordEncoder.encode(userDTO.getSecurityAnswer_2()));
                securityAnswers.add(passwordEncoder.encode(userDTO.getSecurityAnswer_3()));

                userService.addSecurityQuestions(userDTO.getUsername(), securityQuestions, securityAnswers);
            } else {
                userService.createUser(username, displayName, password, false,"ROLE_USER");
            }

        } catch (Exception e) {
            logger.warn("Not possible to create new User");
        }
    }
}
