package ee.desertgun.jttracker.repository;

import ee.desertgun.jttracker.domain.ProfilePicture;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

public interface FileSystemRepository extends JpaRepository<ProfilePicture, UUID> {


    String RESOURCES_DIR = "profile-pictures/";

    default String save(byte[] content, String imageName) throws Exception {
        Path newFile = Paths.get(RESOURCES_DIR + new Date().getTime() + "-" + imageName);
        Files.createDirectories(newFile.getParent());

        Files.write(newFile, content);
        return newFile.toAbsolutePath().toString();
    }

    default FileSystemResource findInFileSystem(String location) {
        try {
            return new FileSystemResource(Paths.get(location));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
