package ee.desertgun.jttracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: Possible future Implementation for a more custom JWT Validation (debatable)
@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class UserAuthController {

    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token provided");
        }
        return ResponseEntity.ok("Token valid according to keycloak");
    }
}
