package ee.desertgun.jttracker.service.jwt;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtUserService {
    public JwtUserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            return extractUserInfo(jwt);
        }

        throw new IllegalStateException("No JWT authentication found");
    }

    public JwtUserInfo extractUserInfo(Jwt jwt) {
        return new JwtUserInfo(
                jwt.getSubject(),
                jwt.getClaimAsString("username"),
                jwt.getClaimAsString("accountName"),
                extractRoles()
        );
    }

    private List<String> extractRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    @Getter
    public static class JwtUserInfo {
        private final String keycloakUserId;
        private final String username;
        private final String accountName;
        private final List<String> roles;

        public JwtUserInfo(String keycloakUserId, String username,
                           String accountName, List<String> roles) {
            this.keycloakUserId = keycloakUserId;
            this.username = username;
            this.accountName = accountName;
            this.roles = roles != null ? roles : List.of();
        }

        // TODO: Future Implementation, after Migration to Kotlin
        public boolean hasRole(String role) {
            return roles.contains("ROLE_" + role) || roles.contains(role);
        }

        public String getDisplayName() {
            if (accountName != null) {
                return accountName;
            }
            return username;
        }
    }
}
