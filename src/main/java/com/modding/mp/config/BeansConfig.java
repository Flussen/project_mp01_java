package com.modding.mp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.modding.mp.adapter.out.security.JwtService;
import com.modding.mp.adapter.out.security.StringPasswordHasher;
import com.modding.mp.application.usecase.LoginUserUseCase;
import com.modding.mp.application.usecase.RegisterUserUseCase;
import com.modding.mp.domain.port.out.IUserRepository;

@Configuration
public class BeansConfig {
    @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
    @Bean StringPasswordHasher stringPasswordHasher(PasswordEncoder encoder) {
        return new StringPasswordHasher(encoder);
    }
    @Bean JwtService jwtService(AppProperties props) {
        return new JwtService(props);
    }

    @Bean RegisterUserUseCase registerUserUseCase(IUserRepository users, StringPasswordHasher hasher) {
        return new RegisterUserUseCase(users, hasher);
    }

    @Bean LoginUserUseCase loginUserUseCase(IUserRepository users, JwtService jwt, StringPasswordHasher hasher) {
        return new LoginUserUseCase(users, jwt, hasher);
    }
}
