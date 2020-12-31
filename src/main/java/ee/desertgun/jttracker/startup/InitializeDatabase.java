package ee.desertgun.jttracker.startup;


import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import ee.desertgun.jttracker.domain.Mail;
import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.dto.TrackedTimeDTO;
import ee.desertgun.jttracker.dto.UserProjectDTO;
import ee.desertgun.jttracker.service.EmailServiceImpl;
import ee.desertgun.jttracker.service.TrackedTimeService;
import ee.desertgun.jttracker.service.UserProjectService;
import ee.desertgun.jttracker.service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        "$2a$10$WoG5Z4YN9Z37EWyNCkltyeFr6PtrSXSLMeFWOeDUwcanht5CIJgPa", "ROLE_USER");


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

      emailService.sendEmail("test@domain.de", "Init-Confirmation-Test", "Test-Online!");
      emailService.sendEmail("admin@domain.de", "Init-Confirmation-Admin", "Admin-Online!");
      Mail testMail = new Mail();
      testMail.setFrom("admin@domain.de");
      testMail.setMailTo("test@domain.de");
      testMail.setSubject("Test-HTML-Sending of Emails");
      emailService.sendComplexMail(testMail);
    }
  }
}
