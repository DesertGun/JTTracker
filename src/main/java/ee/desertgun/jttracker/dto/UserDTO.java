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

    private String securityQuestions;

    private Boolean securityEnabled;

}
