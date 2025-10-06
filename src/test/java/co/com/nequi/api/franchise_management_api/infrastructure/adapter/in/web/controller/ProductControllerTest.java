package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.controller;

import co.com.nequi.api.franchise_management_api.domain.model.Branch;
import co.com.nequi.api.franchise_management_api.domain.model.Product;
import co.com.nequi.api.franchise_management_api.domain.port.in.BranchUseCase;
import co.com.nequi.api.franchise_management_api.domain.port.in.ProductUseCase;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.CreateProductRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.UpdateProductStockRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.ProductResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.mapper.ProductRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ProductUseCase productUseCase;

    @MockitoBean
    private BranchUseCase branchUseCase;

    @MockitoBean
    private ProductRestMapper mapper;

    @Test
    void shouldAddProductToBranch() {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("Big Mac")
                .stock(50)
                .build();

        Product product = Product.builder()
                .id("product-1")
                .name("Big Mac")
                .stock(50)
                .branchId("branch-1")
                .build();

        ProductResponse response = ProductResponse.builder()
                .id("product-1")
                .name("Big Mac")
                .stock(50)
                .branchId("branch-1")
                .build();

        when(mapper.toDomain(any())).thenReturn(product);
        when(productUseCase.addProductToBranch(anyString(), any())).thenReturn(Mono.just(product));
        when(mapper.toResponse(any())).thenReturn(response);

        webTestClient.post()
                .uri("/api/v1/branches/branch-1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("product-1")
                .jsonPath("$.name").isEqualTo("Big Mac")
                .jsonPath("$.stock").isEqualTo(50);
    }

    @Test
    void shouldGetProductById() {
        Product product = Product.builder()
                .id("product-1")
                .name("Big Mac")
                .stock(50)
                .build();

        ProductResponse response = ProductResponse.builder()
                .id("product-1")
                .name("Big Mac")
                .stock(50)
                .build();

        when(productUseCase.getProductById(anyString())).thenReturn(Mono.just(product));
        when(mapper.toResponse(any())).thenReturn(response);

        webTestClient.get()
                .uri("/api/v1/products/product-1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("product-1")
                .jsonPath("$.name").isEqualTo("Big Mac");
    }

    @Test
    void shouldUpdateProductStock() {
        UpdateProductStockRequest request = UpdateProductStockRequest.builder()
                .stock(100)
                .build();

        Product product = Product.builder()
                .id("product-1")
                .name("Big Mac")
                .stock(100)
                .build();

        ProductResponse response = ProductResponse.builder()
                .id("product-1")
                .name("Big Mac")
                .stock(100)
                .build();

        when(productUseCase.updateProductStock(anyString(), any())).thenReturn(Mono.just(product));
        when(mapper.toResponse(any())).thenReturn(response);

        webTestClient.patch()
                .uri("/api/v1/products/product-1/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.stock").isEqualTo(100);
    }

    @Test
    void shouldDeleteProduct() {
        when(productUseCase.deleteProduct(anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/products/product-1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void shouldGetMaxStockProductsByFranchise() {
        Product product = Product.builder()
                .id("product-1")
                .name("Big Mac")
                .stock(100)
                .branchId("branch-1")
                .build();

        Branch branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .build();

        when(productUseCase.getProductsWithMaxStockByFranchise(anyString()))
                .thenReturn(Flux.just(product));
        when(branchUseCase.getBranchById(anyString())).thenReturn(Mono.just(branch));
        when(mapper.toMaxStockResponse(any(), any())).thenReturn(
                Mono.just(co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.MaxStockProductResponse.builder()
                        .productId("product-1")
                        .productName("Big Mac")
                        .stock(100)
                        .branchId("branch-1")
                        .branchName("Centro")
                        .build())
        );

        webTestClient.get()
                .uri("/api/v1/franchises/franchise-1/max-stock-products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.MaxStockProductResponse.class)
                .hasSize(1);
    }

    @Test
    void shouldReturnBadRequestWhenInvalidStock() {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("Big Mac")
                .stock(-10) // Stock negativo
                .build();

        webTestClient.post()
                .uri("/api/v1/branches/branch-1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
