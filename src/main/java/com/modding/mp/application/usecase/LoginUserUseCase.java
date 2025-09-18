package com.modding.mp.application.usecase;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.modding.mp.adapter.out.security.JwtService;
import com.modding.mp.adapter.out.security.StringPasswordHasher;
import com.modding.mp.application.usecase.exceptions.BadRequestException;
import com.modding.mp.application.usecase.exceptions.UnauthorizedException;
import com.modding.mp.config.JwtProps;
import com.modding.mp.domain.model.JWTSession;
import com.modding.mp.domain.model.Session;
import com.modding.mp.domain.model.User;
import com.modding.mp.domain.model.enums.SessionStatus;
import com.modding.mp.domain.port.out.IClockPort;
import com.modding.mp.domain.port.out.ISessionRepository;
import com.modding.mp.domain.port.out.IUserRepository;

@Service
public class LoginUserUseCase {
    private final IUserRepository users;
    private final ISessionRepository sessions;
    private final JwtService jwt;
    private final StringPasswordHasher hasher;
    private final IClockPort clock;
    private final JwtProps props; 

    public LoginUserUseCase(
        IUserRepository users,
        ISessionRepository sessions,
        JwtService jwt,
        StringPasswordHasher hasher,
        IClockPort clock,
        JwtProps props
    ) {
        this.users = users;
        this.sessions = sessions;
        this.jwt = jwt;
        this.hasher = hasher;
        this.clock = clock;
        this.props = props;
    }

    public JWTSession handle(String username, String password, String deviceInfo, String ip) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new BadRequestException("Username and password must be provided");
        }
        User user = users.byUsername(username).orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        if(!hasher.matches(password, user.getPasswordHash())) throw new UnauthorizedException("Invalid credentials");

        // times
        Instant now = clock.now();
        Duration accessTtl  = props.getAccess().getTtl();         
        Duration refreshTtl = props.getRefresh().getTtl();        
        Duration absTtl     = props.getRefresh().getAbsoluteTtl();

        UUID sessionId = UUID.randomUUID();
        UUID refreshJti = UUID.randomUUID();

        Session session = new Session(
            sessionId,
            user.getId().value(),
            refreshJti,
            SessionStatus.ACTIVE,
            now.plus(refreshTtl),
            now.plus(absTtl),
            now,
            now,
            deviceInfo
        );
        sessions.save(session);

        String accessToken = jwt.signAccess(
            user.getId().value(),
            sessionId,
            now,
            accessTtl
        );

        String refreshToken = jwt.signRefresh(
            user.getId().value(),
            sessionId,
            refreshJti,
            now,
            refreshTtl
        );

        return new JWTSession(
            user.getId().value(),
            sessionId,
            accessToken,
            refreshToken,
            now.plus(accessTtl) 
        );
    }
}
