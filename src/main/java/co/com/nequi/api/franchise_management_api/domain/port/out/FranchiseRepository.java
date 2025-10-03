package co.com.nequi.api.franchise_management_api.domain.port.out;

import co.com.nequi.api.franchise_management_api.domain.model.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

    Mono<Franchise> save(Franchise franchise);

    Mono<Franchise> findById(String id);

    Flux<Franchise> findAll();

    Mono<Franchise> update(Franchise franchise);

    Mono<Void> deleteById(String id);

    Mono<Boolean> existsByName(String name);

}
