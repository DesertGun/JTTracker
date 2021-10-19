package ee.desertgun.jttracker.service;

import ee.desertgun.jttracker.domain.Mail;

import javax.mail.MessagingException;

public interface EmailService {

    void sendComplexMail(Mail mail, String template) throws MessagingException;
}
