package com.modding.mp.domain.model;

import java.time.Instant;
import java.util.UUID;

public record JWTSession(
    UUID userId,
    UUID sessionId,
    String accessToken,
    String refreshToken,
    Instant accessExpiresAt
) {}