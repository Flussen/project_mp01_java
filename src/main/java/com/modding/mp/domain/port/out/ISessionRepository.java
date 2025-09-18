package com.modding.mp.domain.port.out;

import java.util.*;

import com.modding.mp.domain.model.Session;

public interface ISessionRepository {
    void save(Session session);
    Optional<Session> findActive(UUID sessionId);
    void update(Session session);
    List<Session> findAllActiveByUser(UUID userId);
}
