package ee.desertgun.jttracker.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class JWTResponse implements Serializable {


    private final String jwtToken;
    private final String error;

    public JWTResponse(String jwtToken, String error) {
        this.jwtToken = jwtToken;
        this.error = error;
    }
}