package com.modding.mp.adapter.out.security;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.modding.mp.config.AppProperties;
import com.modding.mp.domain.model.UserId;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class JwtService {
    private final Algorithm alg;
    private final JWTVerifier verifier;
    private final String issuer;
    private final String audience;
    private final long accessSecs;
    private final long refreshSecs;

    public JwtService(AppProperties props) {
        System.out.println("JWT_SECRET_B64 length = " + props.jwt().secretB64().length());
        var jwt = props.jwt();
        this.issuer = jwt.issuer();
        this.audience = jwt.audience();
        this.accessSecs = jwt.accessMins().toSeconds();
        this.refreshSecs = jwt.refreshDays().toSeconds();

        byte[] key = Base64.getDecoder().decode(jwt.secretB64());
        if (key.length < 32) throw new IllegalStateException("JWT_SECRET_B64 debe ser >= 32 bytes (HS256).");

        this.alg = Algorithm.HMAC256(key);
        this.verifier = JWT.require(alg)
                .withIssuer(issuer)
                .withAudience(audience)
                .acceptLeeway(2)
                .build();
    }

    public String signAccess(UserId userId, String username) {
        var now = Instant.now();
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject(userId.toString())
            .withClaim("uname", username)
            .withClaim("typ", "access")
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(now.plusSeconds(accessSecs)))
            .withJWTId(java.util.UUID.randomUUID().toString())
            .sign(alg);
    }

    public String signRefresh(UserId userId) {
        var now = Instant.now();
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject(userId.toString())
            .withClaim("typ", "refresh")
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(now.plusSeconds(refreshSecs)))
            .withJWTId(java.util.UUID.randomUUID().toString())
            .sign(alg);
    }

    public DecodedJWT verify(String token) {
        return verifier.verify(token);
    }

    public DecodedJWT verifyAccessToken(String token) {
    try {
        var jwt = verifier.verify(token);
        if (!"access".equals(jwt.getClaim("typ").asString())) {
            throw new JWTVerificationException("Token is not an access token");
        }
        return jwt;
    } catch (JWTVerificationException e) {
        throw new IllegalArgumentException("Invalid access token", e);
    }
}

public void verifyRefreshToken(String token) {
    try {
        DecodedJWT decodedJWT = verifier.verify(token);
        if (decodedJWT.getClaim("typ").isNull() || !"refresh".equals(decodedJWT.getClaim("typ").asString())) {
            throw new JWTVerificationException("Token is not a refresh token");
            }
        } catch (JWTVerificationException e) {
            throw new IllegalArgumentException("Invalid refresh token", e);
        }
    }
}
