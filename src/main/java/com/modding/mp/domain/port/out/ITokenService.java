package com.modding.mp.domain.port.out;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface ITokenService {
  String signAccess(
      UUID userId,
      UUID sessionId,
      Instant now,
      Duration ttl);
  String signRefresh(
      UUID userId,
      UUID sessionId,
      UUID refreshJti,
      Instant now,
      Duration ttl);
  DecodedJWT verify(String token);
  DecodedJWT verifyAccessToken(String token);
  DecodedJWT verifyRefreshToken(String token);
}