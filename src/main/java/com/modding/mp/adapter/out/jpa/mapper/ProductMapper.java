package com.modding.mp.adapter.out.jpa.mapper;

import com.modding.mp.adapter.out.jpa.entity.ProductEntity;
import com.modding.mp.domain.model.Product;

public final class ProductMapper {

    private ProductMapper() {}

    public static Product toDomain(ProductEntity entity){
        if(entity == null) return null;
        return new Product(
            entity.getId(),
            entity.getName(),
            entity.getDescription()
            );
    }

    public static ProductEntity toEntity(Product product){
        if(product == null) return null;
        var entity = new ProductEntity();
        if(product.getId() != null) {
            entity.setId(product.getId());
        }
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        return entity;
    }

    public static ProductEntity toEntityRef(Product product){
        if(product == null) return null;
        var entity = new ProductEntity();
        entity.setId(product.getId());
        return entity;
    }
}
