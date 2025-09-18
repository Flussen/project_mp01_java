package com.modding.mp.domain.model;

import java.time.Instant;
import java.util.UUID;

import com.modding.mp.domain.model.enums.SessionStatus;

public record Session(
  UUID id,
  UUID userId,
  UUID refreshJtiCurrent,
  SessionStatus status,
  Instant refreshExpiresAt,
  Instant absExpiresAt,
  Instant lastUsedAt,
  Instant createdAt,
  String deviceInfo
) {}