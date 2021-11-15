package ee.desertgun.jttracker.controller;

import ee.desertgun.jttracker.config.JWTToken;
import ee.desertgun.jttracker.model.JWTRequest;
import ee.desertgun.jttracker.model.JWTResponse;
import ee.desertgun.jttracker.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(UserAuthController.class);


    public UserAuthController(AuthenticationManager authenticationManager, JWTToken jwtToken, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtToken = jwtToken;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JWTRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        } catch (DisabledException e) {
            String error = "User not found!";
            logger.warn(error);
            return ResponseEntity.ok(new JWTResponse(null, error));
        } catch (BadCredentialsException e) {
            String error = "Username or Password incorrect!";
            logger.warn(error);
            return ResponseEntity.ok(new JWTResponse(null, error));
        }
        final UserDetails userDetails = userService.getUserByUsername(authenticationRequest.getUsername());
        final String token = jwtToken.generateToken(userDetails);
        return ResponseEntity.ok(new JWTResponse(token, null));
    }

}
