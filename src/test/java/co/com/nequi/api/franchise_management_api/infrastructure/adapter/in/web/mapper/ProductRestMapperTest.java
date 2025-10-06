package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.mapper;

import co.com.nequi.api.franchise_management_api.domain.model.Branch;
import co.com.nequi.api.franchise_management_api.domain.model.Product;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.CreateProductRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.MaxStockProductResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.ProductResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

public class ProductRestMapperTest {

    private final ProductRestMapper mapper = new ProductRestMapper();

    @Test
    void shouldMapRequestToDomain() {
        CreateProductRequest request = CreateProductRequest.builder().name("Big Mac").stock(50).build();

        Product product = mapper.toDomain(request);

        assertNotNull(product);
        assertEquals("Big Mac", product.getName());
        assertEquals(50, product.getStock());
    }

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        Product product = mapper.toDomain(null);
        assertNull(product);
    }

    @Test
    void shouldMapDomainToResponse() {
        Product product = Product.builder().id("product-1").name("Big Mac").stock(50).branchId("branch-1").build();

        ProductResponse response = mapper.toResponse(product);

        assertNotNull(response);
        assertEquals("product-1", response.getId());
        assertEquals("Big Mac", response.getName());
        assertEquals(50, response.getStock());
        assertEquals("branch-1", response.getBranchId());
    }

    @Test
    void shouldReturnNullWhenDomainIsNull() {
        ProductResponse response = mapper.toResponse(null);
        assertNull(response);
    }

    @Test
    void shouldMapToMaxStockResponse() {
        Product product = Product.builder().id("product-1").name("Big Mac").stock(100).branchId("branch-1").build();

        Branch branch = Branch.builder().id("branch-1").name("Centro").build();

        Mono<MaxStockProductResponse> result = mapper.toMaxStockResponse(product, Mono.just(branch));

        StepVerifier.create(result).expectNextMatches(response -> response.getProductId().equals("product-1") && response.getProductName().equals("Big Mac") && response.getStock().equals(100) && response.getBranchId().equals("branch-1") && response.getBranchName().equals("Centro")).verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenProductIsNull() {
        Branch branch = Branch.builder().id("branch-1").name("Centro").build();

        Mono<MaxStockProductResponse> result = mapper.toMaxStockResponse(null, Mono.just(branch));

        StepVerifier.create(result).verifyComplete();
    }

}
