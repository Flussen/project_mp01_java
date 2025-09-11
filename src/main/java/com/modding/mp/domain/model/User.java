package com.modding.mp.domain.model;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private UserId id;
    private String username;
    private Email email;
    private String passwordHash; 
    private boolean enabled;
    private String discordId;
    private final Instant createdAt;

    // Owned
    private Set<Product> products;

    // Permissions
    private final boolean admin;

    public User(UserId id, String username, Email email, String passwordHash, boolean enabled, String discordId, boolean isAdmin, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.email = email; this.passwordHash = passwordHash;
        this.enabled = enabled; this.discordId = discordId;
        this.admin = isAdmin;
        this.createdAt = createdAt;
        this.products = Collections.emptySet();
    }

    public User(Email email, String username, String passwordHash, boolean enabled, String discordId, boolean isAdmin, Instant createdAt) {
        this.username = username;
        this.email = email; this.passwordHash = passwordHash;
        this.enabled = enabled; this.discordId = discordId;
        this.admin = isAdmin;
        this.createdAt = createdAt;
        this.products = Collections.emptySet();
    }

    public User(UserId id, String username, Email email, String passwordHash, boolean enabled, String discordId, boolean isAdmin, Instant createdAt, Set<Product> products) {
        this.id = id;
        this.username = username;
        this.email = email; this.passwordHash = passwordHash;
        this.enabled = enabled; this.discordId = discordId;
        this.admin = isAdmin;
        this.createdAt = createdAt;
        this.products = products;
    }


    public void linkDiscord(String id) { this.discordId = id; }
}
