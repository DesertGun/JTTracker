package ee.desertgun.jttracker.domain;

import ee.desertgun.jttracker.repository.PasswordTokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@DataJpaTest
public class PasswordResetTokenTest {
    private User user;
    private PasswordResetToken passwordResetToken;

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PasswordTokenRepository passwordTokenRepository;


    @Before
    public void createTestUserAndProfilePicture() {
        User user = new User("");
        user.setUsername("test");
        user = entityManager.persist(user);
        this.user = user;

        UUID passwordResetTokenID = UUID.randomUUID();

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken("test");
        passwordResetToken.setUser(user);
        passwordResetToken.setId(passwordResetTokenID);
        passwordResetToken.setExpiryDate(Date.from(Instant.now()));

        passwordResetToken = entityManager.merge(passwordResetToken);

        this.passwordResetToken = passwordResetToken;
    }


    @Test
    public void saveToken() {

        assertSame(passwordTokenRepository.getById(passwordResetToken.getId()), (passwordResetToken));
    }

    @Test
    public void removeToken() {

        passwordTokenRepository.delete(passwordResetToken);

        assertTrue(passwordTokenRepository.findById(passwordResetToken.getId()).isEmpty());
    }

    @Test
    public void validToken() {
        Instant instantNow = Instant.now();
        Instant instantToken = Instant.from(passwordResetToken.getExpiryDate().toInstant());
        Instant diff = Instant.ofEpochSecond(ChronoUnit.SECONDS.between(instantToken, instantNow));

        assertTrue(diff.getEpochSecond() >= -3600);
    }

    @Test
    public void invalidToken() {
        Instant instantNow = Instant.now();
        Instant instantToken = Instant.from(passwordResetToken.getExpiryDate().toInstant());
        instantToken = instantToken.plusSeconds(3660);
        Instant diff = Instant.ofEpochSecond(ChronoUnit.SECONDS.between(instantToken, instantNow));

        assertFalse(diff.getEpochSecond() >= -3600);
    }
}
