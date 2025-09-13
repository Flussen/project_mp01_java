package com.modding.mp.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.modding.mp.adapter.out.security.JwtService;

import org.springframework.lang.NonNull;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(0)
    SecurityFilterChain apiSecurity(HttpSecurity http, JwtService jwtService) throws Exception {
        return http
            .securityMatcher("/**")
            .csrf(csrf -> csrf.disable())
            // .cors(cors -> {})
            .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v1/auth/**").permitAll()
                .requestMatchers("/v1/actuator/health", "/v1/error").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType("application/json");
                res.getWriter().write("{\"error\":\"unauthorized\"}");
            }))
            .addFilterBefore(new AccessTokenFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new RefreshOnlyOnPathFilter(jwtService, "/v1/auth/refresh"), AccessTokenFilter.class)
            .build();
    }

    // === Filtros ===

    private static class AccessTokenFilter extends OncePerRequestFilter {
        private final JwtService jwt;
        AccessTokenFilter(JwtService jwt) { this.jwt = jwt; }

        @Override
        protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
            )
                throws ServletException, IOException {

            String auth = request.getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                String token = auth.substring(7);
                try {
                    DecodedJWT decoded = jwt.verifyAccessToken(token);
                    String subject = decoded.getSubject();
                    String[] roles = decoded.getClaim("roles").asArray(String.class);

                    var authorities = (roles == null)
                        ? List.<SimpleGrantedAuthority>of()
                        : Arrays.stream(roles).map(SimpleGrantedAuthority::new).toList();

                    var authentication = new UsernamePasswordAuthenticationToken(subject, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                    // opcional: devolver 401 inmediato
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
            chain.doFilter(request, response);
        }
    }

    private static class RefreshOnlyOnPathFilter extends OncePerRequestFilter {
        private final JwtService jwt;
        private final String path;

        RefreshOnlyOnPathFilter(JwtService jwt, String path) {
            this.jwt = jwt; this.path = path;
        }

        @Override
        protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
            // empareja ruta real con contextPath
            String expected = request.getContextPath() + path;
            String uri = request.getRequestURI();
            return !uri.equals(expected);
        }

        @Override
        protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
          )
                throws ServletException, IOException {
            String token = request.getHeader("Refresh-Token");
            if (token != null) {
                try {
                    jwt.verifyRefreshToken(token);
                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"invalid_refresh_token\"}");
                    return;
                }
            }
            chain.doFilter(request, response);
        }
    }

    // === CORS ===

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // tu frontend
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization","Refresh-Token"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
