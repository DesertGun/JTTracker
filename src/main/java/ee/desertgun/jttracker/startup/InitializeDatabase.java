package ee.desertgun.jttracker.startup;


import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import ee.desertgun.jttracker.domain.Mail;
import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.dto.TrackedTimeDTO;
import ee.desertgun.jttracker.dto.UserProjectDTO;
import ee.desertgun.jttracker.service.email.EmailServiceImpl;
import ee.desertgun.jttracker.service.timer.TrackedTimeService;
import ee.desertgun.jttracker.service.user.UserService;
import ee.desertgun.jttracker.service.userproject.UserProjectService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@AutoConfigureBefore
public class InitializeDatabase implements InitializingBean {

    private static final String USERNAME_MAIN = "main@jttracker.de";
    private static final String USERNAME_TEST = "test@jttracker.de";
    private final UserService userService;
    private final TrackedTimeService trackedTimeService;
    private final UserProjectService userProjectService;
    private final EmailServiceImpl emailService;

    @Autowired
    public InitializeDatabase(final UserService userService, final EmailServiceImpl emailService,
                              final TrackedTimeService trackedTimeService, final UserProjectService userProjectService) {
        this.userService = userService;
        this.emailService = emailService;
        this.userProjectService = userProjectService;
        this.trackedTimeService = trackedTimeService;
    }

    @Override
    public void afterPropertiesSet() throws MessagingException {
        try {
            userService.loadUserByUsername(USERNAME_MAIN);
        } catch (UsernameNotFoundException ex) {

            final User user = userService.createUser(USERNAME_MAIN,
                    "Main",
                    "$2a$10$WoG5Z4YN9Z37EWyNCkltyeFr6PtrSXSLMeFWOeDUwcanht5CIJgPa", true, "ROLE_USER");

            user.addRole("ROLE_USER");
            final String securityQuestion_1 = "What is the name of your first pet?";
            final String securityQuestion_2 = "What is the name of your first school?";
            final String securityQuestion_3 = "What was your first job?";

            final List<String> securityQuestions = new ArrayList<>();
            securityQuestions.add(securityQuestion_1);
            securityQuestions.add(securityQuestion_2);
            securityQuestions.add(securityQuestion_3);

            final String securityAnswer_1 = "$2a$09$QxTekzzvceCk1HHlodd0fOcKQdokWiDNUZObNLBIs7uHxBr8SsLk.";
            final String securityAnswer_2 = "$2a$09$HcDpjpUwfiIpYiLzpU7Xj.rgLcrK0dWpP4q7AUXigwj70drDYZ.TC";
            final String securityAnswer_3 = "$2a$09$pfMvtmRb62IR8I.2W7iz4eQA4q76X2FKe7rvGux02Jm1xtsjNhLsu";

            final List<String> securityAnswers = new ArrayList<>();
            securityAnswers.add(securityAnswer_1);
            securityAnswers.add(securityAnswer_2);
            securityAnswers.add(securityAnswer_3);

            userService.addSecurityQuestions(USERNAME_MAIN, securityQuestions, securityAnswers);

            final TrackedTimeDTO trackedTimeDTO = new TrackedTimeDTO();

            Instant t1 = Instant.parse("2019-12-03T10:15:30.00Z");
            Instant a1 = Instant.parse("2019-12-03T13:15:30.00Z");
            UUID timeID1 = UUID.randomUUID();
            Duration duration = java.time.Duration.between(t1, a1);

            trackedTimeDTO.setDuration(duration);
            trackedTimeDTO.setEndTime(a1);
            trackedTimeDTO.setStartTime(t1);
            trackedTimeDTO.setTimeDesc("Pause");
            trackedTimeDTO.setTimeID(timeID1);

            trackedTimeService.createTime(trackedTimeDTO, user.getUsername());

            UUID projectID = UUID.randomUUID();
            String projectName = "test";
            String projectDesc = "Test-Description";
            String priority = "High";
            Boolean status = true;

            UserProjectDTO userProjectDTO = new UserProjectDTO();

            userProjectDTO.setPriority(priority);
            userProjectDTO.setProjectDesc(projectDesc);
            userProjectDTO.setProjectID(projectID);
            userProjectDTO.setProjectName(projectName);
            userProjectDTO.setStatus(status);

            userProjectService.createUserProject(userProjectDTO, user.getUsername());

            GreenMail greenMail = new GreenMail(ServerSetup.ALL);
            greenMail.setUser(USERNAME_MAIN, "password");
            greenMail.setUser(USERNAME_TEST, "password");
            greenMail.start();

            Mail adminMail = new Mail();
            adminMail.setFrom(USERNAME_MAIN);
            adminMail.setMailTo(USERNAME_MAIN);
            adminMail.setSubject("Init-Confirmation-Main");
            Map<String, Object> propAdmin = new HashMap<>();
            propAdmin.put("userName", adminMail.getMailTo());
            adminMail.setProps(propAdmin);
            emailService.sendComplexMail(adminMail, "init");

            Mail testMail = new Mail();
            testMail.setFrom(USERNAME_MAIN);
            testMail.setMailTo(USERNAME_TEST);
            testMail.setSubject("Init-Confirmation-Test");
            Map<String, Object> propTest = new HashMap<>();
            propTest.put("userName", testMail.getMailTo());
            testMail.setProps(propTest);

            emailService.sendComplexMail(testMail, "init");
        }
    }
}
