package ee.desertgun.jttracker.dto;

import lombok.Data;

@Data
public class UserProfileDTO {

    private String username;


    private String accountName;


    private String currentPassword;


    private String newPassword;
}
