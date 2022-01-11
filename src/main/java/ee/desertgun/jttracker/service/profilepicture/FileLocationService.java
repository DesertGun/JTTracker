package ee.desertgun.jttracker.service.profilepicture;

import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.util.UUID;

public interface FileLocationService {
    void save(byte[] bytes, String imageName, String username) throws Exception;

    FileSystemResource find(UUID pictureID);
    void deleteImage(UUID pictureID, String username) throws IOException;
}
