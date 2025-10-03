package co.com.nequi.api.franchise_management_api.domain.port.out;

import co.com.nequi.api.franchise_management_api.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {

    Mono<Branch> save(Branch branch);

    Mono<Branch> findById(String id);

    Flux<Branch> findByFranchiseId(String franchiseId);

    Flux<Branch> findAll();

    Mono<Branch> update(Branch branch);

    Mono<Void> deleteById(String id);

}
