package ee.desertgun.jttracker.service.password;

import ee.desertgun.jttracker.domain.PasswordResetToken;
import ee.desertgun.jttracker.domain.User;
import ee.desertgun.jttracker.repository.PasswordTokenRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collections;

@Service
public class PasswordTokenValidationServiceImpl implements PasswordTokenValidationService {

    private final PasswordTokenRepository passwordTokenRepository;

    public PasswordTokenValidationServiceImpl(final PasswordTokenRepository passwordTokenRepository) {
        this.passwordTokenRepository = passwordTokenRepository;
    }

    public String validatePasswordResetToken(String username, String token) {
        PasswordResetToken passToken =
                passwordTokenRepository.findByToken(token);
        if ((passToken == null) || (!passToken.getUser().getUsername().equals(username))) {
            return "invalidToken";
        }

        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            return "expired";
        }

        User user = passToken.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, null, Collections.singletonList(
                new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return null;
    }
}
