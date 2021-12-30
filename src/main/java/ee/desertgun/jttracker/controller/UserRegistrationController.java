package ee.desertgun.jttracker.controller;

import ee.desertgun.jttracker.domain.Mail;
import ee.desertgun.jttracker.dto.UserDTO;
import ee.desertgun.jttracker.response.ValidationResponse;
import ee.desertgun.jttracker.service.email.EmailService;
import ee.desertgun.jttracker.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class UserRegistrationController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final EmailService emailService;

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
        } else if (userService.userExists(userDTO.getUsername())) {
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

        String accountName = userDTO.getAccountName();
        String username = userDTO.getUsername();
        String password = passwordEncoder.encode(userDTO.getPassword());

        try {
            if (Boolean.TRUE.equals(userDTO.getSecurityQuestionsAvailable())) {
                userService.createUser(username, accountName, password, true, "ROLE_USER");
                UserAccountController.extractEnhancedSecurityDetails(userDTO, passwordEncoder, userService);
            } else {
                userService.createUser(username, accountName, password, false, "ROLE_USER");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
