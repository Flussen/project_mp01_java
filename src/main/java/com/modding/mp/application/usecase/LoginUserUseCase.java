package com.modding.mp.application.usecase;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.modding.mp.adapter.out.security.JwtService;
import com.modding.mp.adapter.out.security.StringPasswordHasher;
import com.modding.mp.application.usecase.exceptions.BadRequestException;
import com.modding.mp.application.usecase.exceptions.UnauthorizedException;
import com.modding.mp.application.usecase.exceptions.UserNotFoundException;
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
        if(username.isEmpty() || passwordTry.isEmpty()) throw new BadRequestException("Username and password must be provided");
        Optional<User> user = users.byUsername(username);

        if(!user.isPresent()) throw new UserNotFoundException(username);
        if(!hasher.matches(passwordTry, user.get().getPasswordHash())) throw new UnauthorizedException("Invalid credentials");

        String accesToken = jwt.signAccess(user.get().getId(), username);
        String refreshToken = jwt.signRefresh(user.get().getId());

        return new JWTSession(user.get().getId(), accesToken, refreshToken);
    }
}
