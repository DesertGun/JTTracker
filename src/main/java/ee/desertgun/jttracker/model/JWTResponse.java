package ee.desertgun.jttracker.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class JWTResponse implements Serializable {


    private final String jwtToken;

    public JWTResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}