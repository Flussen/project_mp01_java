package com.modding.mp.adapter.out.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.modding.mp.config.AppProperties;
import com.modding.mp.domain.port.out.ITokenService;

@Service
public class JwtService implements ITokenService {
    private final Algorithm alg;
    private final JWTVerifier verifier;
    private final String issuer;
    private final String audience;

    private final Duration accessTtlDefault;
    private final Duration refreshTtlDefault;

    public JwtService(AppProperties props) {
        var jwt = props.jwt();
        this.issuer = jwt.issuer();
        this.audience = jwt.audience();

        this.accessTtlDefault = jwt.accessMins();
        this.refreshTtlDefault = jwt.refreshDays();

        byte[] key = Base64.getDecoder().decode(jwt.secretB64());
        if (key.length < 32) {
            throw new IllegalStateException("JWT_SECRET_B64 debe decodificar a >= 32 bytes para HS256.");
        }

        this.alg = Algorithm.HMAC256(key);
        this.verifier = JWT.require(alg)
                .withIssuer(issuer)
                .withAudience(audience)
                .acceptLeeway(120)
                .build();
    }
    public String signAccess(UUID userId,
                             UUID sessionId,
                             Instant now,
                             Duration ttl) {
        if (ttl == null) ttl = accessTtlDefault;
        return JWT.create()
                .withIssuer(issuer)
                .withAudience(audience)
                .withSubject(userId.toString())
                .withClaim("sid", sessionId.toString())
                .withClaim("typ", "access")
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plus(ttl)))
                .withJWTId(UUID.randomUUID().toString())
                .sign(alg);
    }

    public String signRefresh(UUID userId,
                              UUID sessionId,
                              UUID refreshJti,
                              Instant now,
                              Duration ttl) {
        if (ttl == null) ttl = refreshTtlDefault;
        return JWT.create()
                .withIssuer(issuer)
                .withAudience(audience)
                .withSubject(userId.toString())
                .withClaim("sid", sessionId.toString())
                .withClaim("typ", "refresh")
                .withJWTId(refreshJti.toString())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plus(ttl)))
                .sign(alg);
    }


    public DecodedJWT verify(String token) {
        return verifier.verify(token);
    }

    public DecodedJWT verifyAccessToken(String token) {
        try {
            DecodedJWT jwt = verifier.verify(token);
            if (!"access".equals(jwt.getClaim("typ").asString())) {
                throw new JWTVerificationException("Token is not an access token");
            }
            // Validaciones adicionales opcionales:
            // - roles no nulos, sid presente, etc.
            ensureSidPresent(jwt);
            return jwt;
        } catch (JWTVerificationException e) {
            throw new IllegalArgumentException("Invalid access token", e);
        }
    }

    public DecodedJWT verifyRefreshToken(String token) {
        try {
            DecodedJWT jwt = verifier.verify(token);
            if (!"refresh".equals(jwt.getClaim("typ").asString())) {
                throw new JWTVerificationException("Token is not a refresh token");
            }
            ensureSidPresent(jwt);
            ensureJtiPresent(jwt);
            return jwt;
        } catch (JWTVerificationException e) {
            throw new IllegalArgumentException("Invalid refresh token", e);
        }
    }

    private static void ensureSidPresent(DecodedJWT jwt) {
        var sid = jwt.getClaim("sid");
        if (sid == null || sid.isNull() || sid.asString() == null) {
            throw new JWTVerificationException("Missing 'sid' claim");
        }
        UUID.fromString(sid.asString());
    }

    private static void ensureJtiPresent(DecodedJWT jwt) {
        var jti = jwt.getId();
        if (jti == null || jti.isBlank()) {
            throw new JWTVerificationException("Missing 'jti' in refresh token");
        }
        UUID.fromString(jti);
    }
}
