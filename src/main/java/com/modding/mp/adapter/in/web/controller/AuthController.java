package com.modding.mp.adapter.in.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modding.mp.adapter.in.web.request.LoginRequest;
import com.modding.mp.adapter.in.web.request.RegisterRequest;
import com.modding.mp.adapter.in.web.response.StandardResponse;
import com.modding.mp.application.usecase.LoginUserUseCase;
import com.modding.mp.application.usecase.RegisterUserUseCase;
import com.modding.mp.domain.model.Email;
import com.modding.mp.domain.model.UserId;

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
    public ResponseEntity<StandardResponse<String>> login(@RequestBody @Valid LoginRequest req) {
        login.handle(req.username(), req.password());
        return ResponseEntity.ok(StandardResponse.okMsg("in progres..."));
    }

    @PostMapping("/register")
    public ResponseEntity<StandardResponse<String>> register(@RequestBody @Valid RegisterRequest req) {
        UserId id = register.handle(req.username(), new Email(req.email()), req.password());
        return ResponseEntity.ok(StandardResponse.okMsg(id.toString()));
    }
}
