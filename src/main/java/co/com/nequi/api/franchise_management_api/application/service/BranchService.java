package co.com.nequi.api.franchise_management_api.application.service;

import co.com.nequi.api.franchise_management_api.domain.exception.BranchNotFoundException;
import co.com.nequi.api.franchise_management_api.domain.exception.FranchiseNotFoundException;
import co.com.nequi.api.franchise_management_api.domain.model.Branch;
import co.com.nequi.api.franchise_management_api.domain.port.in.BranchUseCase;
import co.com.nequi.api.franchise_management_api.domain.port.out.BranchRepository;
import co.com.nequi.api.franchise_management_api.domain.port.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchService implements BranchUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Branch> addBranchToFranchise(String franchiseId, Branch branch) {
        log.info("Adding branch '{}' to franchise id: {}", branch.getName(), franchiseId);

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    if (branch.getId() == null || branch.getId().isEmpty()) {
                        branch.setId(UUID.randomUUID().toString());
                    }
                    branch.setFranchiseId(franchiseId);
                    return branchRepository.save(branch);
                })
                .doOnSuccess(created -> log.info("Branch created successfully with id: {}", created.getId()))
                .doOnError(error -> log.error("Error creating branch: {}", error.getMessage()));
    }

    @Override
    public Mono<Branch> getBranchById(String id) {
        log.info("Getting branch by id: {}", id);

        return branchRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Branch not found with id: {}", id);
                    return Mono.error(new BranchNotFoundException(id));
                }))
                .doOnSuccess(branch -> log.info("Branch found: {}", branch.getName()));
    }

    @Override
    public Flux<Branch> getBranchesByFranchiseId(String franchiseId) {
        log.info("Getting branches for franchise id: {}", franchiseId);

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMapMany(franchise -> branchRepository.findByFranchiseId(franchiseId))
                .doOnComplete(() -> log.info("Branches retrieved for franchise: {}", franchiseId))
                .doOnError(error -> log.error("Error retrieving branches: {}", error.getMessage()));
    }

    @Override
    public Mono<Branch> updateBranchName(String id, String newName) {
        log.info("Updating branch name. Id: {}, New name: {}", id, newName);

        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(id)))
                .flatMap(branch -> {
                    branch.setName(newName);
                    return branchRepository.update(branch);
                })
                .doOnSuccess(updated -> log.info("Branch name updated successfully: {}", updated.getName()))
                .doOnError(error -> log.error("Error updating branch name: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteBranch(String id) {
        log.info("Deleting branch with id: {}", id);

        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(id)))
                .flatMap(branch -> branchRepository.deleteById(id))
                .doOnSuccess(v -> log.info("Branch deleted successfully: {}", id))
                .doOnError(error -> log.error("Error deleting branch: {}", error.getMessage()));
    }
}