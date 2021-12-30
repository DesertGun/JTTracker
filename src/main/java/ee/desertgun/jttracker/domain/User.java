package ee.desertgun.jttracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
public class User implements UserDetails {

    @Id
    @Column(unique = true)
    private String username;

    private String accountName;

    @JsonIgnore
    private String password;

    private String hash;

    private UUID profilePictureID;

    private String securityQuestion1;

    private String securityQuestion2;

    private String securityQuestion3;

    private String securityAnswer1;

    private String securityAnswer2;

    private String securityAnswer3;

    private Boolean securityEnabled;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    protected User() {

    }

    public User(String test) {

    }

    public User(final String username, final String accountName, final String password, final String hash, final Boolean securityEnabled) {
        this.username = username;
        this.accountName = accountName;
        this.password = password;
        this.hash = hash;
        this.securityEnabled = securityEnabled;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(roles.toArray(new String[0]));
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addRole(String role) {
        if (roles == null) {
            this.roles = new ArrayList<>();
        }

        this.roles.add(role);
    }
}
