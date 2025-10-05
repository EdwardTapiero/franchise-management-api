package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.controller;

import co.com.nequi.api.franchise_management_api.domain.port.in.BranchUseCase;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.CreateBranchRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.UpdateBranchNameRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.BranchResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.mapper.BranchRestMapper;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BranchController {

    private final BranchUseCase branchUseCase;
    private final BranchRestMapper mapper;

    @PostMapping(
            value = "/franchises/{franchiseId}/branches",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BranchResponse> addBranchToFranchise(
            @PathVariable String franchiseId,
            @Valid @RequestBody CreateBranchRequest request) {

        log.info("REST: Adding branch '{}' to franchise id: {}", request.getName(), franchiseId);

        return Mono.just(request)
                .map(mapper::toDomain)
                .flatMap(branch -> branchUseCase.addBranchToFranchise(franchiseId, branch))
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Branch created with id: {}", response.getId()));
    }

    @GetMapping(
            value = "/branches/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<BranchResponse> getBranchById(@PathVariable String id) {
        log.info("REST: Getting branch by id: {}", id);

        return branchUseCase.getBranchById(id)
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Branch retrieved: {}", response.getName()));
    }

    @GetMapping(
            value = "/franchises/{franchiseId}/branches",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Flux<BranchResponse> getBranchesByFranchiseId(@PathVariable String franchiseId) {
        log.info("REST: Getting branches for franchise id: {}", franchiseId);

        return branchUseCase.getBranchesByFranchiseId(franchiseId)
                .map(mapper::toResponse)
                .doOnComplete(() -> log.info("REST: Branches retrieved for franchise: {}", franchiseId));
    }

    @PatchMapping(
            value = "/branches/{id}/name",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<BranchResponse> updateBranchName(
            @PathVariable String id,
            @Valid @RequestBody UpdateBranchNameRequest request) {

        log.info("REST: Updating branch name. Id: {}, New name: {}", id, request.getName());

        return branchUseCase.updateBranchName(id, request.getName())
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Branch name updated: {}", response.getName()));
    }

    @DeleteMapping("/branches/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBranch(@PathVariable String id) {
        log.info("REST: Deleting branch with id: {}", id);

        return branchUseCase.deleteBranch(id)
                .doOnSuccess(v -> log.info("REST: Branch deleted: {}", id));
    }

}
