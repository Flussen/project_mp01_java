package com.modding.mp.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.modding.mp.adapter.out.security.JwtService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(0)
    SecurityFilterChain apiSecurity(HttpSecurity http, JwtService jwtService) throws Exception {
        http
            .securityMatcher("/**")
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/actuator/health", "/error").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(new AccessTokenFilter(jwtService), BasicAuthenticationFilter.class)
            .addFilterBefore(new RefreshTokenFilter(jwtService), AccessTokenFilter.class);
        return http.build();
    }

    private static class AccessTokenFilter extends BasicAuthenticationFilter {
        private final JwtService jwtService;

        public AccessTokenFilter(JwtService jwtService) {
            super(authenticationManager -> null);
            this.jwtService = jwtService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                throws java.io.IOException, jakarta.servlet.ServletException {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                try {
                    DecodedJWT decoded = jwtService.verifyAccessToken(token);
                    String subject = decoded.getSubject();
                    var auth = new UsernamePasswordAuthenticationToken(subject, null, List.of());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                }
            }
            chain.doFilter(request, response);
        }
    }

    private static class RefreshTokenFilter extends BasicAuthenticationFilter {
        private final JwtService jwtService;

        public RefreshTokenFilter(JwtService jwtService) {
            super(authenticationManager -> null);
            this.jwtService = jwtService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                throws java.io.IOException, jakarta.servlet.ServletException {
            String token = request.getHeader("Refresh-Token");
            if (token != null) {
                try {
                    jwtService.verifyRefreshToken(token);
                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                }
            }
            chain.doFilter(request, response);
        }
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // frontend
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*")); 
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
