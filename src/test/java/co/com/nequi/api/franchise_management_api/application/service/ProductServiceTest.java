package co.com.nequi.api.franchise_management_api.application.service;

import co.com.nequi.api.franchise_management_api.domain.exception.BranchNotFoundException;
import co.com.nequi.api.franchise_management_api.domain.model.Branch;
import co.com.nequi.api.franchise_management_api.domain.model.Product;
import co.com.nequi.api.franchise_management_api.domain.port.out.BranchRepository;
import co.com.nequi.api.franchise_management_api.domain.port.out.FranchiseRepository;
import co.com.nequi.api.franchise_management_api.domain.port.out.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Branch branch;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id("product-1")
                .name("Big Mac")
                .stock(50)
                .branchId("branch-1")
                .build();

        branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .franchiseId("franchise-1")
                .build();
    }

    @Test
    void shouldAddProductToBranch() {
        when(branchRepository.findById(anyString())).thenReturn(Mono.just(branch));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        Mono<Product> result = productService.addProductToBranch("branch-1", product);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("Big Mac"))
                .verifyComplete();

        verify(branchRepository).findById("branch-1");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldThrowErrorWhenBranchNotFound() {
        when(branchRepository.findById(anyString())).thenReturn(Mono.empty());

        Mono<Product> result = productService.addProductToBranch("non-existent", product);

        StepVerifier.create(result)
                .expectError(BranchNotFoundException.class)
                .verify();

        verify(branchRepository).findById("non-existent");
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldGetProductById() {
        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));

        Mono<Product> result = productService.getProductById("product-1");

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getId().equals("product-1"))
                .verifyComplete();

        verify(productRepository).findById("product-1");
    }

    @Test
    void shouldUpdateProductStock() {
        Product updated = Product.builder()
                .id("product-1")
                .name("Big Mac")
                .stock(100)
                .branchId("branch-1")
                .build();

        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(productRepository.update(any(Product.class))).thenReturn(Mono.just(updated));

        Mono<Product> result = productService.updateProductStock("product-1", 100);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getStock().equals(100))
                .verifyComplete();

        verify(productRepository).findById("product-1");
        verify(productRepository).update(any(Product.class));
    }

    @Test
    void shouldThrowErrorWhenNegativeStock() {
        Mono<Product> result = productService.updateProductStock("product-1", -10);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("cannot be negative"))
                .verify();

        verify(productRepository, never()).findById(any());
    }

    @Test
    void shouldDeleteProduct() {
        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(productRepository.deleteById(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = productService.deleteProduct("product-1");

        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).findById("product-1");
        verify(productRepository).deleteById("product-1");
    }
}
