package com.modding.mp.domain.model;

import java.util.UUID;

public record UserId(UUID value) {
    public static UserId newId(){
        return new UserId(UUID.randomUUID());
    }

    @Override
    public String toString(){
        return value.toString();
    }
}
