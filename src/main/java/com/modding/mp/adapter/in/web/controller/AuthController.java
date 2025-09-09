package com.modding.mp.adapter.in.web.controller;

import org.glassfish.jaxb.core.api.impl.NameConverter.Standard;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modding.mp.adapter.in.web.request.RegisterRequest;
import com.modding.mp.adapter.in.web.response.StandardResponse;
import com.modding.mp.application.usecase.RegisterUserUseCase;
import com.modding.mp.domain.model.Email;
import com.modding.mp.domain.model.UserId;

import org.springframework.web.bind.annotation.PostMapping;


@RestController @RequestMapping("/auth")
public class AuthController {
    private final RegisterUserUseCase register;
    private final PasswordEncoder encoder;

    public AuthController(RegisterUserUseCase register, PasswordEncoder encoder){
        this.register = register; this.encoder = encoder;
    }

    // public ResponseEntity<StandardResponse<String>> login(L)

    @PostMapping("/register")
    public ResponseEntity<StandardResponse<String>> register(@RequestBody RegisterRequest req) {
        UserId id = register.handle(req.username(), new Email(req.email()), encoder.encode(req.password()));
        return ResponseEntity.ok(StandardResponse.okMsg(id.toString()));
    }
}
