package ee.desertgun.jttracker.service;


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
    public void sendComplexMail(final String to, final String subject, final String text) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // true = multipart

        final Context ctx = new Context();
        final String htmlContent = this.templateEngine.process("test.html", ctx);
        helper.setText(htmlContent, true); // true = isHtml

            helper.setFrom("admin@domain.de");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text,true);

            javaMailSender.send(mimeMessage);

    }


}
