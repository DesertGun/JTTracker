package ee.desertgun.jttracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
public class TrackedTime {
    private Instant startTime;

    private Instant endTime;

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID timeID;

    private String timeDesc;

    private Duration duration;

    private String loggedYear;

    private int loggedDayOfYear;

    private String loggedMonth;

    // TODO: mark Timers which are in a Project
    private Boolean inProject;

    @ElementCollection
    @JsonIgnore
    private List<UUID> projectIDs;

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

}
