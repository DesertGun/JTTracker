package ee.desertgun.jttracker.service;


import ee.desertgun.jttracker.domain.Mail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
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

    public EmailServiceImpl(@Qualifier("getJavaMailSender") JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }


    @Async
    public void sendEmail(final String to, final String subject, final String message) {
        final StringBuilder finalMessage = new StringBuilder();
        finalMessage.append(message).append("\n\n");

        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setFrom("admin@domain.de");
        email.setSubject(subject);
        email.setText(finalMessage.toString());

        javaMailSender.send(email);
    }

    @Async
    public void sendComplexMail(Mail mail) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariables(mail.getProps());

        String html = templateEngine.process("test", context);

        helper.setTo(mail.getMailTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());

        javaMailSender.send(message);
    }
}
