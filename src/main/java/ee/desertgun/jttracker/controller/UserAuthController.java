package ee.desertgun.jttracker.controller;

import ee.desertgun.jttracker.config.JWTToken;
import ee.desertgun.jttracker.model.JWTRequest;
import ee.desertgun.jttracker.model.JWTResponse;
import ee.desertgun.jttracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class UserAuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTToken jwtToken;
    private final UserService userService;

    public UserAuthController(AuthenticationManager authenticationManager, JWTToken jwtToken, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtToken = jwtToken;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JWTRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userService.getUserByUsername(authenticationRequest.getUsername());
        final String token = jwtToken.generateToken(userDetails);
        return ResponseEntity.ok(new JWTResponse(token));
    }

    //TODO: Create better responses for the user, instead of 401's
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
