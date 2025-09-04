package com.modding.mp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.modding.mp.application.usecase.RegisterUserUseCase;
import com.modding.mp.domain.port.out.UserRepository;

@Configuration
public class BeansConfig {
    @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}

    @Bean RegisterUserUseCase registerUserUseCase(UserRepository users) {
        return new RegisterUserUseCase(users);
    }
}
