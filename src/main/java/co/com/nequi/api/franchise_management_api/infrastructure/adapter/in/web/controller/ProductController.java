package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.controller;

import co.com.nequi.api.franchise_management_api.domain.port.in.BranchUseCase;
import co.com.nequi.api.franchise_management_api.domain.port.in.ProductUseCase;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.CreateProductRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.UpdateProductNameRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.UpdateProductStockRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.MaxStockProductResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.ProductResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.mapper.ProductRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductUseCase productUseCase;
    private final BranchUseCase branchUseCase;
    private final ProductRestMapper mapper;

    @PostMapping(
            value = "/branches/{branchId}/products",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductResponse> addProductToBranch(
            @PathVariable String branchId,
            @Valid @RequestBody CreateProductRequest request) {

        log.info("REST: Adding product '{}' to branch id: {}", request.getName(), branchId);

        return Mono.just(request)
                .map(mapper::toDomain)
                .flatMap(product -> productUseCase.addProductToBranch(branchId, product))
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Product created with id: {}", response.getId()));
    }

    @GetMapping(
            value = "/products/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductResponse> getProductById(@PathVariable String id) {
        log.info("REST: Getting product by id: {}", id);

        return productUseCase.getProductById(id)
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Product retrieved: {}", response.getName()));
    }

    @GetMapping(
            value = "/branches/{branchId}/products",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Flux<ProductResponse> getProductsByBranchId(@PathVariable String branchId) {
        log.info("REST: Getting products for branch id: {}", branchId);

        return productUseCase.getProductsByBranchId(branchId)
                .map(mapper::toResponse)
                .doOnComplete(() -> log.info("REST: Products retrieved for branch: {}", branchId));
    }

    @PatchMapping(
            value = "/products/{id}/stock",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductResponse> updateProductStock(
            @PathVariable String id,
            @Valid @RequestBody UpdateProductStockRequest request) {

        log.info("REST: Updating product stock. Id: {}, New stock: {}", id, request.getStock());

        return productUseCase.updateProductStock(id, request.getStock())
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Product stock updated: {}", response.getStock()));
    }

    @PatchMapping(
            value = "/products/{id}/name",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductResponse> updateProductName(
            @PathVariable String id,
            @Valid @RequestBody UpdateProductNameRequest request) {

        log.info("REST: Updating product name. Id: {}, New name: {}", id, request.getName());

        return productUseCase.updateProductName(id, request.getName())
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Product name updated: {}", response.getName()));
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(@PathVariable String id) {
        log.info("REST: Deleting product with id: {}", id);

        return productUseCase.deleteProduct(id)
                .doOnSuccess(v -> log.info("REST: Product deleted: {}", id));
    }

    /**
     * Endpoint que muestra el producto con mayor stock por sucursal de una franquicia.
     * Demuestra el uso de operadores reactivos: zip, flatMap, map
     */
    @GetMapping(
            value = "/franchises/{franchiseId}/max-stock-products",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Flux<MaxStockProductResponse> getMaxStockProductsByFranchise(
            @PathVariable String franchiseId) {

        log.info("REST: Getting max stock products for franchise id: {}", franchiseId);

        return productUseCase.getProductsWithMaxStockByFranchise(franchiseId)
                .flatMap(product -> {
                    // Obtener la sucursal para incluir su nombre en la respuesta
                    return branchUseCase.getBranchById(product.getBranchId())
                            .flatMap(branch -> mapper.toMaxStockResponse(product, Mono.just(branch)));
                })
                .doOnComplete(() -> log.info("REST: Max stock products retrieved for franchise: {}", franchiseId));
    }
}
