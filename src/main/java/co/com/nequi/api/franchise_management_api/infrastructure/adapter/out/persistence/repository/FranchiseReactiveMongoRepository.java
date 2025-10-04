package co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.repository;

import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.entity.FranchiseEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FranchiseReactiveMongoRepository extends ReactiveMongoRepository<FranchiseEntity, String> {

    Mono<Boolean> existsByName(String name);

    Mono<FranchiseEntity> findByName(String name);
}