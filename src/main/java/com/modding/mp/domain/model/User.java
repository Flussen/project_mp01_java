package com.modding.mp.domain.model;

import java.time.Instant;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private UserId id;
    private Email email;
    private String passwordHash; 
    private boolean enabled;
    private String discordId;
    private final Set<String> roles;
    private final Instant createdAt;

    public User(UserId id, Email email, String passwordHash, boolean enabled, String discordId, Set<String> roles, Instant createdAt) {
        if (roles == null || roles.isEmpty()) throw new IllegalArgumentException("rol_required");
        this.id = id;
        this.email = email; this.passwordHash = passwordHash;
        this.enabled = enabled; this.discordId = discordId;
        this.roles = Set.copyOf(roles);
        this.createdAt = createdAt;
    }

    public User(Email email, String passwordHash, boolean enabled, String discordId, Set<String> roles, Instant createdAt) {
        if (roles == null || roles.isEmpty()) throw new IllegalArgumentException("rol_required");
        this.email = email; this.passwordHash = passwordHash;
        this.enabled = enabled; this.discordId = discordId;
        this.roles = Set.copyOf(roles);
        this.createdAt = createdAt;
    }


    public void linkDiscord(String id) { this.discordId = id; }
}
