package com.modding.mp.application.usecase;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.modding.mp.adapter.out.security.JwtService;
import com.modding.mp.domain.model.JWTSession;
import com.modding.mp.domain.model.User;
import com.modding.mp.domain.port.out.UserRepository;

public class LoginUserUseCase {
    private final UserRepository users;
    private final JwtService jwt;

    public LoginUserUseCase(UserRepository users, JwtService jwt) {this.users = users; this.jwt = jwt;}

    public JWTSession handle(String username, String passwordTry, PasswordEncoder encoder) {
        if(username.isEmpty() || passwordTry.isEmpty()) throw new Error("Error, empty parameters!");
        Optional<User> user = users.byUsername(username);

        if(!user.isPresent()) throw new Error("User not found!");
        if(!encoder.matches(passwordTry, user.get().getPasswordHash())) throw new Error("Invalid Password!");

        String accesToken = jwt.signAccess(user.get().getId(), username);
        String refreshToken = jwt.signRefresh(user.get().getId());

        return new JWTSession(user.get().getId(), accesToken, refreshToken);
    }
}
