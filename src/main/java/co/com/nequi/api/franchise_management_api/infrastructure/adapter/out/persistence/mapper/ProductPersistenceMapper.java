package co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.mapper;

import co.com.nequi.api.franchise_management_api.domain.model.Product;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductPersistenceMapper {

    public ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }

        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .stock(entity.getStock())
                .branchId(entity.getBranchId())
                .build();
    }

}
