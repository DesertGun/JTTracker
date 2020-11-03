package ee.desertgun.jttracker.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class JWTRequest implements Serializable {

    private String username;

    private String password;

    public JWTRequest() {

    }

    public JWTRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }
}
