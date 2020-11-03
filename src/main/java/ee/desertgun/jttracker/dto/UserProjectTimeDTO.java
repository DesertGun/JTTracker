package ee.desertgun.jttracker.dto;

import ee.desertgun.jttracker.domain.TrackedTime;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Data
public class UserProjectTimeDTO {
    @NotNull
    private UUID projectID;

    private List<TrackedTime> trackedTimeList;

    @NotNull
    private Duration projectTime;
}
