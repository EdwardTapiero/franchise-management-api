package co.com.nequi.api.franchise_management_api.application.service;

import co.com.nequi.api.franchise_management_api.domain.exception.FranchiseNotFoundException;
import co.com.nequi.api.franchise_management_api.domain.model.Franchise;
import co.com.nequi.api.franchise_management_api.domain.port.in.FranchiseUseCase;
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
public class FranchiseService implements FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Franchise> createFranchise(Franchise franchise) {
        log.info("Creating franchise: {}", franchise.getName());

        return Mono.just(franchise)
                .doOnNext(f -> {
                    if (f.getId() == null || f.getId().isEmpty()) {
                        f.setId(UUID.randomUUID().toString());
                    }
                })
                .flatMap(franchiseMap -> franchiseRepository.existsByName(franchiseMap.getName()))
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        log.warn("Franchise already exists with name: {}", franchise.getName());
                        return Mono.error(new IllegalArgumentException(
                                "Franchise with name '" + franchise.getName() + "' already exists"));
                    }
                    return franchiseRepository.save(franchise);
                })
                .doOnSuccess(created -> log.info("Franchise created successfully with id: {}", created.getId()))
                .doOnError(error -> log.error("Error creating franchise: {}", error.getMessage()));
    }

    @Override
    public Mono<Franchise> getFranchiseById(String id) {
        log.info("Getting franchise by id: {}", id);

        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Franchise not found with id: {}", id);
                    return Mono.error(new FranchiseNotFoundException(id));
                }))
                .doOnSuccess(franchise -> log.info("Franchise found: {}", franchise.getName()));
    }

    @Override
    public Flux<Franchise> getAllFranchises() {
        log.info("Getting all franchises");

        return franchiseRepository.findAll()
                .doOnComplete(() -> log.info("All franchises retrieved"))
                .doOnError(error -> log.error("Error retrieving franchises: {}", error.getMessage()));
    }

    @Override
    public Mono<Franchise> updateFranchiseName(String id, String newName) {
        log.info("Updating franchise name. Id: {}, New name: {}", id, newName);

        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(id)))
                .flatMap(franchise -> {
                    franchise.setName(newName);
                    return franchiseRepository.update(franchise);
                })
                .doOnSuccess(updated -> log.info("Franchise name updated successfully: {}", updated.getName()))
                .doOnError(error -> log.error("Error updating franchise name: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteFranchise(String id) {
        log.info("Deleting franchise with id: {}", id);

        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(id)))
                .flatMap(franchise -> franchiseRepository.deleteById(id))
                .doOnSuccess(v -> log.info("Franchise deleted successfully: {}", id))
                .doOnError(error -> log.error("Error deleting franchise: {}", error.getMessage()));
    }
}
