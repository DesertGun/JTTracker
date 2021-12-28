package ee.desertgun.jttracker.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Setter
@Getter
public class ProfilePicture {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Lob
    private byte[] content;

    private String name;

    private String location;

    private String username;

    public ProfilePicture(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public ProfilePicture() {

    }
}
