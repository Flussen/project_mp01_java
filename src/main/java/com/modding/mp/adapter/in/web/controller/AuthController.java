package com.modding.mp.adapter.in.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.modding.mp.adapter.in.web.request.*;
import com.modding.mp.adapter.in.web.response.StandardResponse;
import com.modding.mp.application.usecase.LoginUserUseCase;
import com.modding.mp.application.usecase.RegisterUserUseCase;
import com.modding.mp.domain.model.*;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;


@RestController @RequestMapping("/auth")
public class AuthController {
    private final RegisterUserUseCase register;
    private final LoginUserUseCase login;

    public AuthController(RegisterUserUseCase register, LoginUserUseCase login, PasswordEncoder encoder){
        this.register = register; this.login = login;
    }

    @PostMapping("/login")
    public ResponseEntity<StandardResponse<JWTSession>> login(@RequestBody @Valid LoginRequest req) {
        JWTSession session = login.handle(req.username(), req.password());
        return ResponseEntity.ok(StandardResponse.ok(session, "Login successful"));
    }

    @PostMapping("/register")
    public ResponseEntity<StandardResponse<JWTSession>> register(@RequestBody @Valid RegisterRequest req) {
        JWTSession session = register.handle(req.username(), new Email(req.email()), req.password());
        return ResponseEntity.ok(StandardResponse.ok(session, "Registration successful"));
    }
}
