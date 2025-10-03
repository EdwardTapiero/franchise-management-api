package co.com.nequi.api.franchise_management_api.domain.port.in;

import co.com.nequi.api.franchise_management_api.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchUseCase {

    Mono<Branch> addBranchToFranchise(String franchiseId, Branch branch);

    Mono<Branch> getBranchById(String id);

    Flux<Branch> getBranchesByFranchiseId(String franchiseId);

    Mono<Branch> updateBranchName(String id, String newName);

    Mono<Void> deleteBranch(String id);

}
