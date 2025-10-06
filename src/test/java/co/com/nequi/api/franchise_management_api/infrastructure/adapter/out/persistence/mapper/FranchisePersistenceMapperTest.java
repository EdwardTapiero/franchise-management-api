package co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.mapper;

import co.com.nequi.api.franchise_management_api.domain.model.Franchise;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.entity.FranchiseEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FranchisePersistenceMapperTest {

    private final FranchisePersistenceMapper mapper = new FranchisePersistenceMapper();

    @Test
    void shouldMapDomainToEntity() {
        Franchise franchise = Franchise.builder()
                .id("franchise-1")
                .name("McDonalds")
                .build();

        FranchiseEntity entity = mapper.toEntity(franchise);

        assertNotNull(entity);
        assertEquals("franchise-1", entity.getId());
        assertEquals("McDonalds", entity.getName());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void shouldReturnNullWhenDomainIsNull() {
        FranchiseEntity entity = mapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void shouldMapEntityToDomain() {
        FranchiseEntity entity = FranchiseEntity.builder()
                .id("franchise-1")
                .name("McDonalds")
                .build();

        Franchise franchise = mapper.toDomain(entity);

        assertNotNull(franchise);
        assertEquals("franchise-1", franchise.getId());
        assertEquals("McDonalds", franchise.getName());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        Franchise franchise = mapper.toDomain(null);
        assertNull(franchise);
    }

}
