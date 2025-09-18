package com.modding.mp.config;

import java.time.Instant;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.modding.mp.adapter.out.security.StringPasswordHasher;
import com.modding.mp.domain.port.out.IClockPort;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class BeansConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    StringPasswordHasher stringPasswordHasher(PasswordEncoder encoder) {
        return new StringPasswordHasher(encoder);
    }

    @Bean
    IClockPort clockPort() {
        return new IClockPort() {
            @Override public Instant now() { return Instant.now(); }
        };
    }

}
