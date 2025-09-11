package com.modding.mp.domain.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mod extends Product {
    public Mod(UUID id, String name, String description) {
        super(name, description);
    }

    public Mod(String name, String description) {
        super(name, description);
    }
}
