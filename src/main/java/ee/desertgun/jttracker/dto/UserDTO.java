package ee.desertgun.jttracker.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String accountName;

    private String password;

    private String matchingPassword;

    private String oldPassword;

    private String newPassword;

    private String username;

    private Boolean securityQuestionsAvailable;

    private String securityQuestion1;

    private String securityQuestion2;

    private String securityQuestion3;

    private String securityAnswer1;

    private String securityAnswer2;

    private String securityAnswer3;

}
