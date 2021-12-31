package ee.desertgun.jttracker.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Setter
@Getter
public class ProfilePicture {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;

    private String location;

    public ProfilePicture(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public ProfilePicture() {

    }
}
