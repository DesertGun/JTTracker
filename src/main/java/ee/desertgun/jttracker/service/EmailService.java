package ee.desertgun.jttracker.service;

public interface EmailService {
  void sendEmail(final String to, final String subject, final String message);

}
