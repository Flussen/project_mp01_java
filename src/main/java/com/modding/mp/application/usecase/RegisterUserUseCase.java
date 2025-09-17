package com.modding.mp.application.usecase;

import java.time.Instant;

import com.modding.mp.adapter.out.security.StringPasswordHasher;
import com.modding.mp.domain.model.*;
import com.modding.mp.domain.port.out.IUserRepository;

public class RegisterUserUseCase {
    private final IUserRepository users;
    private final StringPasswordHasher hasher;
    public RegisterUserUseCase(IUserRepository users, StringPasswordHasher hasher) {this.users = users; this.hasher = hasher; }
     
    public UserId handle(String username, Email email, String password) {
        if(users.existsByEmail(email)) throw new IllegalArgumentException("email is not available");

        String passwordHashed = hasher.hash(password);
        User user = new User(email, username, passwordHashed, true, null, false, Instant.now());
        User createdUser = users.save(user);

        return createdUser.getId();
    }
}
