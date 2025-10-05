package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.controller;

import co.com.nequi.api.franchise_management_api.domain.port.in.FranchiseUseCase;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.CreateFranchiseRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.UpdateFranchiseNameRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.FranchiseResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.mapper.FranchiseRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseUseCase franchiseUseCase;
    private final FranchiseRestMapper mapper;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranchiseResponse> createFranchise(
            @Valid @RequestBody CreateFranchiseRequest request) {

        log.info("REST: Creating franchise with name: {}", request.getName());

        return Mono.just(request)
                .map(mapper::toDomain)
                .flatMap(franchiseUseCase::createFranchise)
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Franchise created with id: {}", response.getId()));
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<FranchiseResponse> getFranchiseById(@PathVariable String id) {
        log.info("REST: Getting franchise by id: {}", id);

        return franchiseUseCase.getFranchiseById(id)
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Franchise retrieved: {}", response.getName()));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<FranchiseResponse> getAllFranchises() {
        log.info("REST: Getting all franchises");

        return franchiseUseCase.getAllFranchises()
                .map(mapper::toResponse)
                .doOnComplete(() -> log.info("REST: All franchises retrieved"));
    }

    @PatchMapping(
            value = "/{id}/name",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<FranchiseResponse> updateFranchiseName(
            @PathVariable String id,
            @Valid @RequestBody UpdateFranchiseNameRequest request) {

        log.info("REST: Updating franchise name. Id: {}, New name: {}", id, request.getName());

        return franchiseUseCase.updateFranchiseName(id, request.getName())
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Franchise name updated: {}", response.getName()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteFranchise(@PathVariable String id) {
        log.info("REST: Deleting franchise with id: {}", id);

        return franchiseUseCase.deleteFranchise(id)
                .doOnSuccess(v -> log.info("REST: Franchise deleted: {}", id));
    }

}
