package ee.desertgun.jttracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class TrackedTime {
    private Instant startTime;

    private Instant endTime;

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID timeID;

    private String timeDesc;

    private Duration duration;

    @ManyToMany
    @JsonIgnore
    private List<UserProject> projectList;

    @OneToOne
    @JsonIgnore
    private User user;

    public TrackedTime(final User user, final UUID timeID, final Instant startTime, final Instant endTime,
            final String timeDesc, final Duration duration) {
        this.timeID = timeID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeDesc = timeDesc;
        this.user = user;
        this.duration = duration;
    }

    public TrackedTime() {

    }

    public void addProject(UserProject userProject){
        this.projectList.add(userProject);
        userProject.getTrackedTimeList().add(this);
    }

    public void removeProject(UserProject userProject){
        this.projectList.remove(userProject);
        userProject.getTrackedTimeList().remove(this);
    }

}
