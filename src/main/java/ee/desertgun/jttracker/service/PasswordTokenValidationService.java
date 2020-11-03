package ee.desertgun.jttracker.service;

public interface PasswordTokenValidationService {
  String validatePasswordResetToken(String username, String token);
}
