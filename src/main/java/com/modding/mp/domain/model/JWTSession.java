package com.modding.mp.domain.model;

public record JWTSession(UserId userId, String accessToken, String refreshToken) {
    public static JWTSession of(UserId userId, String accessToken, String refreshToken) {
        return new JWTSession(userId, accessToken, refreshToken);
    }
}
