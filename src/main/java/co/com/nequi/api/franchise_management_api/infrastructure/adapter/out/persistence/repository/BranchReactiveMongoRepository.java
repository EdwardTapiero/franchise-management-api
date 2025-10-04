package co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.repository;

import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.entity.BranchEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BranchReactiveMongoRepository extends ReactiveMongoRepository<BranchEntity, String> {

    Flux<BranchEntity> findByFranchiseId(String franchiseId);

    Flux<BranchEntity> findByFranchiseIdOrderByNameAsc(String franchiseId);
}
