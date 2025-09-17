package com.modding.mp.application.usecase;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.modding.mp.adapter.out.security.JwtService;
import com.modding.mp.adapter.out.security.StringPasswordHasher;
import com.modding.mp.domain.model.*;
import com.modding.mp.domain.port.out.IUserRepository;

@Service
public class RegisterUserUseCase {
    private final IUserRepository users;
    private final StringPasswordHasher hasher;
    private final JwtService jwt;
    public RegisterUserUseCase(IUserRepository users, StringPasswordHasher hasher, JwtService jwt) {this.users = users; this.hasher = hasher; this.jwt = jwt; }
     
    public JWTSession handle(String username, Email email, String password) {
        if(users.existsByEmail(email)) throw new IllegalArgumentException("email is not available");

        String passwordHashed = hasher.hash(password);
        User user = new User(email, username, passwordHashed, true, null, false, Instant.now());
        User createdUser = users.save(user);

        String accesToken = jwt.signAccess(createdUser.getId(), username);
        String refreshToken = jwt.signRefresh(createdUser.getId());

        return new JWTSession(createdUser.getId(), accesToken, refreshToken);
    }
}
