package ee.desertgun.jttracker.service.password;

public interface PasswordTokenValidationService {
  String validatePasswordResetToken(String username, String token);
}
