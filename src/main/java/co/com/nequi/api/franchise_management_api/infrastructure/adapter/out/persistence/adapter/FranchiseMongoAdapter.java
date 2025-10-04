package co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.adapter;

import co.com.nequi.api.franchise_management_api.domain.model.Franchise;
import co.com.nequi.api.franchise_management_api.domain.port.out.FranchiseRepository;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.mapper.FranchisePersistenceMapper;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.repository.FranchiseReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class FranchiseMongoAdapter implements FranchiseRepository {

    private final FranchiseReactiveMongoRepository mongoRepository;
    private final FranchisePersistenceMapper mapper;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        log.debug("Saving franchise: {}", franchise.getName());
        return Mono.just(franchise)
                .map(mapper::toEntity)
                .flatMap(mongoRepository::save)
                .map(mapper::toDomain)
                .doOnSuccess(saved -> log.info("Franchise saved with id: {}", saved.getId()))
                .doOnError(error -> log.error("Error saving franchise: {}", error.getMessage()));
    }

    @Override
    public Mono<Franchise> findById(String id) {
        log.debug("Finding franchise by id: {}", id);
        return mongoRepository.findById(id)
                .map(mapper::toDomain)
                .doOnSuccess(found -> log.info("Franchise found: {}", found != null ? found.getName() : "null"))
                .doOnError(error -> log.error("Error finding franchise: {}", error.getMessage()));
    }

    @Override
    public Flux<Franchise> findAll() {
        log.debug("Finding all franchises");
        return mongoRepository.findAll()
                .map(mapper::toDomain)
                .doOnComplete(() -> log.info("All franchises retrieved"))
                .doOnError(error -> log.error("Error finding all franchises: {}", error.getMessage()));
    }

    @Override
    public Mono<Franchise> update(Franchise franchise) {
        log.debug("Updating franchise: {}", franchise.getId());
        return mongoRepository.findById(franchise.getId())
                .flatMap(existing -> {
                    existing.setName(franchise.getName());
                    return mongoRepository.save(existing);
                })
                .map(mapper::toDomain)
                .doOnSuccess(updated -> log.info("Franchise updated: {}", updated.getId()))
                .doOnError(error -> log.error("Error updating franchise: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        log.debug("Deleting franchise by id: {}", id);
        return mongoRepository.deleteById(id)
                .doOnSuccess(v -> log.info("Franchise deleted: {}", id))
                .doOnError(error -> log.error("Error deleting franchise: {}", error.getMessage()));
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        log.debug("Checking if franchise exists by name: {}", name);
        return mongoRepository.existsByName(name)
                .doOnNext(exists -> log.info("Franchise exists: {}", exists));
    }

}
