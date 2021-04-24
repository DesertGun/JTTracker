package ee.desertgun.jttracker.domain;

import ee.desertgun.jttracker.config.UserProfileGravatarHash;
import ee.desertgun.jttracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
class UserTest {
    private User user;

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        String username = "admin@domain.de";
        String hash = UserProfileGravatarHash.md5Hex(username);

        User user = new User(username,
                "Bob",
                "$2a$10$WoG5Z4YN9Z37EWyNCkltyeFr6PtrSXSLMeFWOeDUwcanht5CIJgPa", hash);
        user.addRole("ROLE_USER");
        user = entityManager.persistAndFlush(user);

        this.user = user;

    }


    @Test
    void getUsername() {
        assertSame(userRepository.findByUsername(user.getUsername()), user);
    }

    @Test
    void getPassword() {
        assertSame(userRepository.findByUsername(user.getUsername()).getPassword(),
                "$2a$10$WoG5Z4YN9Z37EWyNCkltyeFr6PtrSXSLMeFWOeDUwcanht5CIJgPa");
    }

    @Test
    void setUsername() {
        String result = "";
        if (userRepository.existsUserByUsername(user.getUsername())){
            result = "User exists";
        }else{
            user.setUsername("testNew");
            userRepository.save(user);
            result = user.getUsername();
        }

        assertNotSame(result, userRepository.findByUsername(user.getUsername()).getUsername());

    }

    @Test
    void setPassword() {
        String origPw = user.getPassword();
        String newPw = "$2y$12$rJqk6pcWQFFROTlRZZ4/uOicZrVr2wGvsIYfQYuCl/iXy/6iy0uBu";
        user.setPassword(newPw);
        userRepository.save(user);
        assertNotSame(userRepository.findByUsername(user.getUsername()).getPassword(), origPw);
    }

    @Test
    void setAccountName() {
        String origAccountName = user.getAccountName();
        String newAccountName = "Tester";
        user.setAccountName(newAccountName);
        userRepository.save(user);
        assertNotSame(userRepository.findByUsername(user.getUsername()).getAccountName(), origAccountName);
    }

    @Test
    void getAccountName() {
        assertSame("Bob", userRepository.findByUsername(user.getUsername()).getAccountName());
    }
}