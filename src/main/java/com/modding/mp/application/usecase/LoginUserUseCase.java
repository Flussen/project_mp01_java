package com.modding.mp.application.usecase;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.modding.mp.adapter.out.security.JwtService;
import com.modding.mp.adapter.out.security.StringPasswordHasher;
import com.modding.mp.domain.model.JWTSession;
import com.modding.mp.domain.model.User;
import com.modding.mp.domain.port.out.IUserRepository;

@Service
public class LoginUserUseCase {
    private final IUserRepository users;
    private final JwtService jwt;
    private final StringPasswordHasher hasher;

    public LoginUserUseCase(IUserRepository users, JwtService jwt, StringPasswordHasher hasher)
    {
        this.users = users; this.jwt = jwt; this.hasher = hasher;
    }

    public JWTSession handle(String username, String passwordTry) {
        if(username.isEmpty() || passwordTry.isEmpty()) throw new Error("Error, empty parameters!");
        Optional<User> user = users.byUsername(username);

        if(!user.isPresent()) throw new Error("User not found!");
        if(!hasher.matches(passwordTry, user.get().getPasswordHash())) throw new Error("Invalid Password!");

        String accesToken = jwt.signAccess(user.get().getId(), username);
        String refreshToken = jwt.signRefresh(user.get().getId());

        return new JWTSession(user.get().getId(), accesToken, refreshToken);
    }
}
