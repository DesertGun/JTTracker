package ee.desertgun.jttracker.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.UUID;

@Entity
@Setter
@Getter
public class ProfilePicture {

    @Id
    @GeneratedValue
    UUID id;

    @Lob
    byte[] content;

    String name;

    String location;

    String username;

    public ProfilePicture(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public ProfilePicture() {

    }
}
