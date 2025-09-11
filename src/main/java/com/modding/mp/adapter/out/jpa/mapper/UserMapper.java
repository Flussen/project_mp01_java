package com.modding.mp.adapter.out.jpa.mapper;

import com.modding.mp.adapter.out.jpa.entity.UserEntity;
import com.modding.mp.domain.model.Email;
import com.modding.mp.domain.model.User;
import com.modding.mp.domain.model.UserId;

public final class UserMapper {
    public static User toDomain(UserEntity e) {
        return new User(
            new UserId(e.getId()),
            e.getUsername(),
            new Email(e.getEmail()),
            e.getPasswordHash(),
            e.isEnabled(),
            e.getDiscordId(),
            e.isAdmin(),
            e.getCreatedAt());
    }

    public static UserEntity toEntity(User u) {
        var e = new UserEntity();
        e.setUsername(u.getUsername());
        e.setEmail(u.getEmail().value());
        e.setPasswordHash(u.getPasswordHash());
        e.setEnabled(u.isEnabled());
        e.setDiscordId(u.getDiscordId());
        e.setAdmin(u.isAdmin());
        e.setCreatedAt(u.getCreatedAt());
        return e;
    }
}
