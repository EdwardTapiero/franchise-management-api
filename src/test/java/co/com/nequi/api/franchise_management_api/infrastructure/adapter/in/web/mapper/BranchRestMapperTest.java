package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.mapper;

import co.com.nequi.api.franchise_management_api.domain.model.Branch;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.CreateBranchRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.BranchResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BranchRestMapperTest {

    private final BranchRestMapper mapper = new BranchRestMapper();

    @Test
    void shouldMapRequestToDomain() {
        CreateBranchRequest request = CreateBranchRequest.builder()
                .name("Centro")
                .build();

        Branch branch = mapper.toDomain(request);

        assertNotNull(branch);
        assertEquals("Centro", branch.getName());
    }

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        Branch branch = mapper.toDomain(null);
        assertNull(branch);
    }

    @Test
    void shouldMapDomainToResponse() {
        Branch branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .franchiseId("franchise-1")
                .build();

        BranchResponse response = mapper.toResponse(branch);

        assertNotNull(response);
        assertEquals("branch-1", response.getId());
        assertEquals("Centro", response.getName());
        assertEquals("franchise-1", response.getFranchiseId());
    }

    @Test
    void shouldReturnNullWhenDomainIsNull() {
        BranchResponse response = mapper.toResponse(null);
        assertNull(response);
    }

}
