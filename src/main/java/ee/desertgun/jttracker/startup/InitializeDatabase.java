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
            userService.loadUserByUsername("admin@domain.de");
        } catch (UsernameNotFoundException ex) {
            List<String> roles = new ArrayList<String>();
            roles.add("ROLE_USER");

            final User user = userService.createUser("admin@domain.de",
                    "Bob",
                    "$2a$10$WoG5Z4YN9Z37EWyNCkltyeFr6PtrSXSLMeFWOeDUwcanht5CIJgPa", true, "ROLE_USER");

            final String securityQuestions = "What is the name of your first pet?," +
                    "What is the name of your first school?," +
                    "What was your first job?";
            final String securityAnswers = "Test,Test,Test";

            userService.addSecurityQuestions("admin@domain.de", securityQuestions, securityAnswers);


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
            greenMail.setUser("admin@domain.de", "password");
            greenMail.setUser("test@domain.de", "password");
            greenMail.start();
            // greenMail.stop();

            Mail adminMail = new Mail();
            adminMail.setFrom("admin@domain.de");
            adminMail.setMailTo("admin@domain.de");
            adminMail.setSubject("Init-Confirmation-Admin");
            Map<String, Object> propAdmin = new HashMap<>();
            propAdmin.put("userName", adminMail.getMailTo());
            adminMail.setProps(propAdmin);
            emailService.sendComplexMail(adminMail, "init");


            Mail testMail = new Mail();
            testMail.setFrom("admin@domain.de");
            testMail.setMailTo("test@domain.de");
            testMail.setSubject("Init-Confirmation-Test");
            Map<String, Object> propTest = new HashMap<>();
            propTest.put("userName", testMail.getMailTo());
            testMail.setProps(propTest);
            emailService.sendComplexMail(testMail, "init");
        }
    }
}
