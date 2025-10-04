package co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.adapter;

import co.com.nequi.api.franchise_management_api.domain.model.Product;
import co.com.nequi.api.franchise_management_api.domain.port.out.ProductRepository;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.mapper.ProductPersistenceMapper;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.repository.ProductReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductMongoAdapter implements ProductRepository {

    private final ProductReactiveMongoRepository mongoRepository;
    private final ProductPersistenceMapper mapper;

    @Override
    public Mono<Product> save(Product product) {
        log.debug("Saving product: {}", product.getName());
        return Mono.just(product)
                .map(mapper::toEntity)
                .flatMap(mongoRepository::save)
                .map(mapper::toDomain)
                .doOnSuccess(saved -> log.info("Product saved with id: {}", saved.getId()))
                .doOnError(error -> log.error("Error saving product: {}", error.getMessage()));
    }

    @Override
    public Mono<Product> findById(String id) {
        log.debug("Finding product by id: {}", id);
        return mongoRepository.findById(id)
                .map(mapper::toDomain)
                .doOnSuccess(found -> log.info("Product found: {}", found != null ? found.getName() : "null"))
                .doOnError(error -> log.error("Error finding product: {}", error.getMessage()));
    }

    @Override
    public Flux<Product> findByBranchId(String branchId) {
        log.debug("Finding products by branch id: {}", branchId);
        return mongoRepository.findByBranchId(branchId)
                .map(mapper::toDomain)
                .doOnComplete(() -> log.info("Products retrieved for branch: {}", branchId))
                .doOnError(error -> log.error("Error finding products: {}", error.getMessage()));
    }

    @Override
    public Flux<Product> findAll() {
        log.debug("Finding all products");
        return mongoRepository.findAll()
                .map(mapper::toDomain)
                .doOnComplete(() -> log.info("All products retrieved"))
                .doOnError(error -> log.error("Error finding all products: {}", error.getMessage()));
    }

    @Override
    public Mono<Product> update(Product product) {
        log.debug("Updating product: {}", product.getId());
        return mongoRepository.findById(product.getId())
                .flatMap(existing -> {
                    existing.setName(product.getName());
                    existing.setStock(product.getStock());
                    return mongoRepository.save(existing);
                })
                .map(mapper::toDomain)
                .doOnSuccess(updated -> log.info("Product updated: {}", updated.getId()))
                .doOnError(error -> log.error("Error updating product: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        log.debug("Deleting product by id: {}", id);
        return mongoRepository.deleteById(id)
                .doOnSuccess(v -> log.info("Product deleted: {}", id))
                .doOnError(error -> log.error("Error deleting product: {}", error.getMessage()));
    }
}