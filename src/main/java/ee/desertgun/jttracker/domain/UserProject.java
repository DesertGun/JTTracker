package ee.desertgun.jttracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
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

    public void addTime(TrackedTime trackedTime) {
        this.trackedTimeList.add(trackedTime);
        trackedTime.getProjectList().add(this);
    }

    public void removeTime(TrackedTime trackedTime) {
        this.trackedTimeList.remove(trackedTime);
        trackedTime.getProjectList().remove(this);
    }


    public void countDuration(List<TrackedTime> trackedTimes) {
        projectTime = Duration.ZERO;
        List<Duration> durationList = new ArrayList<>();
        for (TrackedTime trackedTime : trackedTimes) {
            durationList.add((trackedTime.getDuration()));
        }
        projectTime = durationList.stream().reduce(Duration.ZERO, Duration::plus);
    }


    public UserProject() {

    }
}
