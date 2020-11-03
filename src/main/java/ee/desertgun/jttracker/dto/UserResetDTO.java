package ee.desertgun.jttracker.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

//TODO: Refactor->Delete

@Data
public class UserResetDTO {

  @NotNull
  private String username;

}
