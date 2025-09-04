package com.modding.mp.adapter.out.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modding.mp.adapter.out.jpa.entity.UserEntity;

interface StringUserJpaRepo extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
