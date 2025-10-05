package co.com.nequi.api.franchise_management_api.application.service;

import co.com.nequi.api.franchise_management_api.domain.exception.BranchNotFoundException;
import co.com.nequi.api.franchise_management_api.domain.exception.FranchiseNotFoundException;
import co.com.nequi.api.franchise_management_api.domain.exception.ProductNotFoundException;
import co.com.nequi.api.franchise_management_api.domain.model.Product;
import co.com.nequi.api.franchise_management_api.domain.port.in.ProductUseCase;
import co.com.nequi.api.franchise_management_api.domain.port.out.BranchRepository;
import co.com.nequi.api.franchise_management_api.domain.port.out.FranchiseRepository;
import co.com.nequi.api.franchise_management_api.domain.port.out.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Product> addProductToBranch(String branchId, Product product) {
        log.info("Adding product '{}' to branch id: {}", product.getName(), branchId);

        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .flatMap(branch -> {
                    if (product.getId() == null || product.getId().isEmpty()) {
                        product.setId(UUID.randomUUID().toString());
                    }
                    product.setBranchId(branchId);
                    return productRepository.save(product);
                })
                .doOnSuccess(created -> log.info("Product created successfully with id: {}", created.getId()))
                .doOnError(error -> log.error("Error creating product: {}", error.getMessage()));
    }

    @Override
    public Mono<Product> getProductById(String id) {
        log.info("Getting product by id: {}", id);

        return productRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Product not found with id: {}", id);
                    return Mono.error(new ProductNotFoundException(id));
                }))
                .doOnSuccess(product -> log.info("Product found: {}", product.getName()));
    }

    @Override
    public Flux<Product> getProductsByBranchId(String branchId) {
        log.info("Getting products for branch id: {}", branchId);

        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .flatMapMany(branch -> productRepository.findByBranchId(branchId))
                .doOnComplete(() -> log.info("Products retrieved for branch: {}", branchId))
                .doOnError(error -> log.error("Error retrieving products: {}", error.getMessage()));
    }

    @Override
    public Mono<Product> updateProductStock(String id, Integer newStock) {
        log.info("Updating product stock. Id: {}, New stock: {}", id, newStock);

        if (newStock < 0) {
            return Mono.error(new IllegalArgumentException("Stock cannot be negative"));
        }

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(product -> {
                    product.setStock(newStock);
                    return productRepository.update(product);
                })
                .doOnSuccess(updated -> log.info("Product stock updated successfully. New stock: {}", updated.getStock()))
                .doOnError(error -> log.error("Error updating product stock: {}", error.getMessage()));
    }

    @Override
    public Mono<Product> updateProductName(String id, String newName) {
        log.info("Updating product name. Id: {}, New name: {}", id, newName);

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(product -> {
                    product.setName(newName);
                    return productRepository.update(product);
                })
                .doOnSuccess(updated -> log.info("Product name updated successfully: {}", updated.getName()))
                .doOnError(error -> log.error("Error updating product name: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteProduct(String id) {
        log.info("Deleting product with id: {}", id);

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(product -> productRepository.deleteById(id))
                .doOnSuccess(v -> log.info("Product deleted successfully: {}", id))
                .doOnError(error -> log.error("Error deleting product: {}", error.getMessage()));
    }

    /**
     * Encuentra el producto con mayor stock por cada sucursal de una franquicia.
     * Este método demuestra el uso de operadores reactivos avanzados:
     * - flatMap: Para transformar flujos
     * - zip: Para combinar múltiples Monos
     * - map: Para transformar datos
     * - switchIfEmpty: Para manejar casos vacíos
     */
    @Override
    public Flux<Product> getProductsWithMaxStockByFranchise(String franchiseId) {
        log.info("Getting products with max stock for franchise id: {}", franchiseId);

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMapMany(franchise -> branchRepository.findByFranchiseId(franchiseId))
                .flatMap(branch ->
                        productRepository.findByBranchId(branch.getId())
                                .collectList()
                                .flatMap(products -> {
                                    if (products.isEmpty()) {
                                        log.debug("No products found for branch: {}", branch.getName());
                                        return Mono.empty();
                                    }

                                    // Encontrar el producto con mayor stock
                                    Product maxStockProduct = products.stream()
                                            .max(Comparator.comparing(Product::getStock))
                                            .orElse(null);

                                    return Mono.justOrEmpty(maxStockProduct);
                                })
                )
                .doOnComplete(() -> log.info("Max stock products retrieved for franchise: {}", franchiseId))
                .doOnError(error -> log.error("Error retrieving max stock products: {}", error.getMessage()));
    }
}