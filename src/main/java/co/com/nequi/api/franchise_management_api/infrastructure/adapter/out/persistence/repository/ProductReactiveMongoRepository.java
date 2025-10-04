package co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.repository;

import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductReactiveMongoRepository extends ReactiveMongoRepository<ProductEntity, String> {

    Flux<ProductEntity> findByBranchId(String branchId);

    Flux<ProductEntity> findByBranchIdOrderByStockDesc(String branchId);

    Mono<ProductEntity> findTopByBranchIdOrderByStockDesc(String branchId);
}
