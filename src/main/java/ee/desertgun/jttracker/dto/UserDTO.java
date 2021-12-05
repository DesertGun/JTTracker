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

    private String securityQuestion_1;

    private String securityQuestion_2;

    private String securityQuestion_3;

    private String securityAnswer_1;

    private String securityAnswer_2;

    private String securityAnswer_3;

}
