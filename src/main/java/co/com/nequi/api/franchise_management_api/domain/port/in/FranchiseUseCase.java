package co.com.nequi.api.franchise_management_api.domain.port.in;

import co.com.nequi.api.franchise_management_api.domain.model.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseUseCase {

    Mono<Franchise> createFranchise(Franchise franchise);

    Mono<Franchise> getFranchiseById(String id);

    Flux<Franchise> getAllFranchises();

    Mono<Franchise> updateFranchiseName(String id, String newName);

    Mono<Void> deleteFranchise(String id);

}
