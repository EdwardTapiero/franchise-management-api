package co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.mapper;

import co.com.nequi.api.franchise_management_api.domain.model.Branch;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.entity.BranchEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BranchPersistenceMapperTest {

    private final BranchPersistenceMapper mapper = new BranchPersistenceMapper();

    @Test
    void shouldMapDomainToEntity() {
        Branch branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .franchiseId("franchise-1")
                .build();

        BranchEntity entity = mapper.toEntity(branch);

        assertNotNull(entity);
        assertEquals("branch-1", entity.getId());
        assertEquals("Centro", entity.getName());
        assertEquals("franchise-1", entity.getFranchiseId());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void shouldReturnNullWhenDomainIsNull() {
        BranchEntity entity = mapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void shouldMapEntityToDomain() {
        BranchEntity entity = BranchEntity.builder()
                .id("branch-1")
                .name("Centro")
                .franchiseId("franchise-1")
                .build();

        Branch branch = mapper.toDomain(entity);

        assertNotNull(branch);
        assertEquals("branch-1", branch.getId());
        assertEquals("Centro", branch.getName());
        assertEquals("franchise-1", branch.getFranchiseId());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        Branch branch = mapper.toDomain(null);
        assertNull(branch);
    }

}
