package ee.desertgun.jttracker.domain;

import ee.desertgun.jttracker.repository.ProfilePictureRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@DataJpaTest
public class ProfilePictureTest {
    private User user;
    private ProfilePicture profilePicture;

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProfilePictureRepository profilePictureRepository;


    @Before
    public void createTestUserAndProfilePicture() {
        User user = new User("");
        user.setUsername("test");
        user = entityManager.persist(user);
        this.user = user;

        UUID pictureID = UUID.randomUUID();
        // The Picture used for this test belongs to Thomas Kinto, @thomaskinto
        // Original source url: https://unsplash.com/photos/x506G6UtlFs
        ProfilePicture profilePicture = new ProfilePicture("Landscape_Forest_Test",
                "../test_resources/Landscape_Forest_Test.jpg");
        profilePicture.setId(pictureID);
        profilePicture.setUsername(user.getUsername());
        profilePicture = entityManager.merge(profilePicture);

        this.profilePicture = profilePicture;
    }


    @Test
    public void savePicture() {

        assertSame(profilePictureRepository.getById(profilePicture.getId()), (profilePicture));
    }

    @Test
    public void removePicture() {

        profilePictureRepository.delete(profilePicture);
        user.setProfilePictureID(null);

        assertTrue(profilePictureRepository.findById(profilePicture.getId()).isEmpty());
    }
}
