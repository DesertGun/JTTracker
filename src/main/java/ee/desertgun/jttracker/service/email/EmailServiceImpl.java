package ee.desertgun.jttracker.service.email;


import ee.desertgun.jttracker.domain.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;


@Service
@Primary
public class EmailServiceImpl implements EmailService {

    public final JavaMailSender javaMailSender;
    public final TemplateEngine templateEngine;
    Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    public EmailServiceImpl(@Qualifier("getJavaMailSender") JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendComplexMail(Mail mail, String template) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariables(mail.getProps());

            String html = templateEngine.process(template, context);

            helper.setTo(mail.getMailTo());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom("noreply@jttracker.de");

            javaMailSender.send(message);
        } catch (MailSendException | MessagingException e) {
            logger.info("Cannot establish connection! Please verify your connection settings in the " +
                    "application-backend.properties prior of building the image!");
        }
    }
}
