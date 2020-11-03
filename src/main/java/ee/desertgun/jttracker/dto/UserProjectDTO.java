package ee.desertgun.jttracker.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class UserProjectDTO {

    @NotNull
    private UUID projectID;

    @NotNull
    private String projectName;


    private String projectDesc;

    @NotNull
    private String priority;

    @NotNull
    private Boolean status;
}
