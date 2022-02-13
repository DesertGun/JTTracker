package ee.desertgun.jttracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
public class UserProject {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID projectID;

    private String projectName;

    private String projectDesc;

    private String priority;

    private Boolean status;

    @ManyToOne
    @JsonIgnore
    private User user;

    @ManyToMany
    private List<TrackedTime> trackedTimeList;

    private Duration projectTime;

    public UserProject(final UUID projectID, final String projectName, final String projectDesc, final String priority,
                       Boolean status) {

        this.projectID = projectID;
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.priority = priority;
        this.status = status;

    }

    public UserProject() {

    }
}
