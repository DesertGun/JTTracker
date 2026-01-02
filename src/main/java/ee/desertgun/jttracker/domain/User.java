package ee.desertgun.jttracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User{

    @Id
    @Column(name = "keycloak_user_id", nullable = false)
    private String keycloakUserId;

    @Column(nullable = false)
    private String username;

    private String accountName;

    private String hash;

    private UUID profilePictureID;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "keycloak_user_id"))
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public User() {
        this.roles = new ArrayList<>();
    }

    public User(String keycloakUserId, String username, String accountName) {
        this.keycloakUserId = keycloakUserId;
        this.username = username;
        this.accountName = accountName;
        this.roles = new ArrayList<>();
    }

    public void addRole(String role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        if (!this.roles.contains(role)) {
            this.roles.add(role);
        }
    }

    public void removeRole(String role) {
        if (this.roles != null) {
            this.roles.remove(role);
        }
    }
}
