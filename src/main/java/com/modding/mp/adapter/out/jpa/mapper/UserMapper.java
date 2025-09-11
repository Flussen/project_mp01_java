package com.modding.mp.adapter.out.jpa.mapper;


import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

import com.modding.mp.adapter.out.jpa.entity.UserEntity;
import com.modding.mp.adapter.out.jpa.entity.UserProductEntity;
import com.modding.mp.domain.model.Email;
import com.modding.mp.domain.model.Product;
import com.modding.mp.domain.model.User;
import com.modding.mp.domain.model.UserId;

public final class UserMapper {
    public static User toDomain(UserEntity e) {
        var products = e.getProductsIds() == null
        ? new HashSet<Product>()
        : e.getProductsIds().stream()
            .map(UserProductEntity::getProduct)
            .filter(Objects::nonNull)
            .map(ProductMapper::toDomain)
            .collect(Collectors.toCollection(HashSet::new));
        
        return new User(
            new UserId(e.getId()),
            e.getUsername(),
            new Email(e.getEmail()),
            e.getPasswordHash(),
            e.isEnabled(),
            e.getDiscordId(),
            e.isAdmin(),
            e.getCreatedAt(),
            products
            );
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

        if(u.getProducts() != null && !u.getProducts().isEmpty()){
            var links = new HashSet<UserProductEntity>();
            for (var pDomain : u.getProducts()){
                var pEntity = ProductMapper.toEntityRef(pDomain);
                var link = new UserProductEntity();
                link.setUser(e);
                link.setProduct(pEntity);
                links.add(link);
            }
            e.setProductsIds(links);
        }else {
            e.setProductsIds(new HashSet<>());
        }
        return e;
    }
}
