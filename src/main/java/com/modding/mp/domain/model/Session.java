package com.modding.mp.domain.model;

import java.time.Instant;

public class Session {
    private UserId userId;
    private String sessionId;
    private boolean isValid;
    private Instant expiresAt;
    private final Instant createdAt;
}
