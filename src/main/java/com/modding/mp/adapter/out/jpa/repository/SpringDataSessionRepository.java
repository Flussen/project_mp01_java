package com.modding.mp.adapter.out.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.modding.mp.adapter.out.jpa.entity.SessionEntity;

public interface SpringDataSessionRepository extends JpaRepository<SessionEntity, UUID> {
    Optional<SessionEntity> findByIdAndStatus(UUID id, String status);
    List<SessionEntity> findAllByUserIdAndStatus(UUID userId, String status);
}