package com.modding.mp.application.usecase;

import org.springframework.stereotype.Service;
import com.modding.mp.adapter.out.security.JwtService;
import com.modding.mp.domain.model.JWTSession;
import com.modding.mp.domain.port.out.ITokenService;

@Service
public class RefreshTokenUseCase {
    private final ITokenService jwt;

    public RefreshTokenUseCase(JwtService jwt) {
        this.jwt = jwt;
    }

    public JWTSession handle(String refreshToken) {
        jwt.verifyRefreshToken(refreshToken);
        
        return null;
    }
}
