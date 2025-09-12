package com.modding.mp.adapter.out.security;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtService {
    private final Algorithm alg;
    private final JWTVerifier verifier;
    private final String issuer;
    private final String audience;
    private final long accessSecs;
    private final long refreshSecs;

    public JwtService(byte[] secretKey, String issuer, String audience, long accessSecs, long refreshSecs) {
        this.alg = Algorithm.HMAC256(secretKey);
        this.issuer = issuer;
        this.audience = audience;
        this.accessSecs = accessSecs;
        this.refreshSecs = refreshSecs;
        this.verifier = JWT
            .require(alg)
            .withIssuer(issuer)
            .withAudience(audience)
            .acceptLeeway(2) 
            .build();
    }

    public String signAccess(String userId, String username, Set<String> roles) {
        var now = Instant.now();
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject(userId)
            .withClaim("uname", username)
            .withArrayClaim("roles", roles.toArray(String[]::new))
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(now.plusSeconds(accessSecs)))
            .withJWTId(java.util.UUID.randomUUID().toString())
            .sign(alg);
    }

    public String signRefresh(String userId) {
        var now = Instant.now();
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject(userId)
            .withClaim("typ", "refresh")
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(now.plusSeconds(refreshSecs)))
            .withJWTId(java.util.UUID.randomUUID().toString())
            .sign(alg);
    }

    public DecodedJWT verify(String token) {
        return verifier.verify(token);
    }
}
