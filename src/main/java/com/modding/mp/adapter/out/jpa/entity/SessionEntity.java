package com.modding.mp.adapter.out.jpa.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "sessions")
@Getter @Setter
public class SessionEntity {
    @Id @Column(name = "session_id") @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false) private UUID userId;
    @Column(nullable = false) private UUID refreshJtiCurrent;
    @Column(nullable = false) private Instant refreshExpiresAt;
    @Column(nullable = false) private Instant absExpiresAt;
    @Column(nullable = false) private Instant lastUsedAt;
    @Column(nullable = false) private Instant createdAt;

    private String deviceInfo;
    private byte[] ipHash;
}
