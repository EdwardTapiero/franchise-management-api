package co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.adapter;

import co.com.nequi.api.franchise_management_api.domain.model.Branch;
import co.com.nequi.api.franchise_management_api.domain.port.out.BranchRepository;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.mapper.BranchPersistenceMapper;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.repository.BranchReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BranchMongoAdapter implements BranchRepository {

    private final BranchReactiveMongoRepository mongoRepository;
    private final BranchPersistenceMapper mapper;

    @Override
    public Mono<Branch> save(Branch branch) {
        log.debug("Saving branch: {}", branch.getName());
        return Mono.just(branch)
                .map(mapper::toEntity)
                .flatMap(mongoRepository::save)
                .map(mapper::toDomain)
                .doOnSuccess(saved -> log.info("Branch saved with id: {}", saved.getId()))
                .doOnError(error -> log.error("Error saving branch: {}", error.getMessage()));
    }

    @Override
    public Mono<Branch> findById(String id) {
        log.debug("Finding branch by id: {}", id);
        return mongoRepository.findById(id)
                .map(mapper::toDomain)
                .doOnSuccess(found -> log.info("Branch found: {}", found != null ? found.getName() : "null"))
                .doOnError(error -> log.error("Error finding branch: {}", error.getMessage()));
    }

    @Override
    public Flux<Branch> findByFranchiseId(String franchiseId) {
        log.debug("Finding branches by franchise id: {}", franchiseId);
        return mongoRepository.findByFranchiseId(franchiseId)
                .map(mapper::toDomain)
                .doOnComplete(() -> log.info("Branches retrieved for franchise: {}", franchiseId))
                .doOnError(error -> log.error("Error finding branches: {}", error.getMessage()));
    }

    @Override
    public Flux<Branch> findAll() {
        log.debug("Finding all branches");
        return mongoRepository.findAll()
                .map(mapper::toDomain)
                .doOnComplete(() -> log.info("All branches retrieved"))
                .doOnError(error -> log.error("Error finding all branches: {}", error.getMessage()));
    }

    @Override
    public Mono<Branch> update(Branch branch) {
        log.debug("Updating branch: {}", branch.getId());
        return mongoRepository.findById(branch.getId())
                .flatMap(existing -> {
                    existing.setName(branch.getName());
                    return mongoRepository.save(existing);
                })
                .map(mapper::toDomain)
                .doOnSuccess(updated -> log.info("Branch updated: {}", updated.getId()))
                .doOnError(error -> log.error("Error updating branch: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        log.debug("Deleting branch by id: {}", id);
        return mongoRepository.deleteById(id)
                .doOnSuccess(v -> log.info("Branch deleted: {}", id))
                .doOnError(error -> log.error("Error deleting branch: {}", error.getMessage()));
    }

}
