package ee.desertgun.jttracker.dto;

import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Data
public class TrackedTimeDTO {
    @NotNull
    private Instant startTime;

    @NotNull
    private Instant endTime;

    @NotNull
    private Duration duration;

    @Id
    @NotNull
    private UUID timeID;

    private String timeDesc;

}
