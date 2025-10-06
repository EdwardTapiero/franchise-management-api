package co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.mapper;

import co.com.nequi.api.franchise_management_api.domain.model.Product;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductPersistenceMapperTest {

    private final ProductPersistenceMapper mapper = new ProductPersistenceMapper();

    @Test
    void shouldMapDomainToEntity() {
        Product product = Product.builder()
                .id("product-1")
                .name("Big Mac")
                .stock(50)
                .branchId("branch-1")
                .build();

        ProductEntity entity = mapper.toEntity(product);

        assertNotNull(entity);
        assertEquals("product-1", entity.getId());
        assertEquals("Big Mac", entity.getName());
        assertEquals(50, entity.getStock());
        assertEquals("branch-1", entity.getBranchId());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void shouldReturnNullWhenDomainIsNull() {
        ProductEntity entity = mapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void shouldMapEntityToDomain() {
        ProductEntity entity = ProductEntity.builder()
                .id("product-1")
                .name("Big Mac")
                .stock(50)
                .branchId("branch-1")
                .build();

        Product product = mapper.toDomain(entity);

        assertNotNull(product);
        assertEquals("product-1", product.getId());
        assertEquals("Big Mac", product.getName());
        assertEquals(50, product.getStock());
        assertEquals("branch-1", product.getBranchId());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        Product product = mapper.toDomain(null);
        assertNull(product);
    }

}
