package com.modding.mp.domain.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Product {
    private UUID id;
    private String name;
    private String description;

    public Product(String name, String description){
        this.name = name;
        this.description = description;
    }
}
