package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.controller;

import co.com.nequi.api.franchise_management_api.domain.model.Branch;
import co.com.nequi.api.franchise_management_api.domain.port.in.BranchUseCase;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.CreateBranchRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.UpdateBranchNameRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.BranchResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.mapper.BranchRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(BranchController.class)
public class BranchControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BranchUseCase branchUseCase;

    @MockitoBean
    private BranchRestMapper mapper;

    @Test
    void shouldAddBranchToFranchise() {
        CreateBranchRequest request = CreateBranchRequest.builder()
                .name("Centro")
                .build();

        Branch branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .franchiseId("franchise-1")
                .build();

        BranchResponse response = BranchResponse.builder()
                .id("branch-1")
                .name("Centro")
                .franchiseId("franchise-1")
                .build();

        when(mapper.toDomain(any())).thenReturn(branch);
        when(branchUseCase.addBranchToFranchise(anyString(), any())).thenReturn(Mono.just(branch));
        when(mapper.toResponse(any())).thenReturn(response);

        webTestClient.post()
                .uri("/api/v1/franchises/franchise-1/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("branch-1")
                .jsonPath("$.name").isEqualTo("Centro")
                .jsonPath("$.franchiseId").isEqualTo("franchise-1");
    }

    @Test
    void shouldGetBranchById() {
        Branch branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .build();

        BranchResponse response = BranchResponse.builder()
                .id("branch-1")
                .name("Centro")
                .build();

        when(branchUseCase.getBranchById(anyString())).thenReturn(Mono.just(branch));
        when(mapper.toResponse(any())).thenReturn(response);

        webTestClient.get()
                .uri("/api/v1/branches/branch-1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("branch-1")
                .jsonPath("$.name").isEqualTo("Centro");
    }

    @Test
    void shouldGetBranchesByFranchiseId() {
        Branch branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .franchiseId("franchise-1")
                .build();

        BranchResponse response = BranchResponse.builder()
                .id("branch-1")
                .name("Centro")
                .franchiseId("franchise-1")
                .build();

        when(branchUseCase.getBranchesByFranchiseId(anyString())).thenReturn(Flux.just(branch));
        when(mapper.toResponse(any())).thenReturn(response);

        webTestClient.get()
                .uri("/api/v1/franchises/franchise-1/branches")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BranchResponse.class)
                .hasSize(1);
    }

    @Test
    void shouldUpdateBranchName() {
        UpdateBranchNameRequest request = UpdateBranchNameRequest.builder()
                .name("Centro Actualizado")
                .build();

        Branch branch = Branch.builder()
                .id("branch-1")
                .name("Centro Actualizado")
                .build();

        BranchResponse response = BranchResponse.builder()
                .id("branch-1")
                .name("Centro Actualizado")
                .build();

        when(branchUseCase.updateBranchName(anyString(), anyString())).thenReturn(Mono.just(branch));
        when(mapper.toResponse(any())).thenReturn(response);

        webTestClient.patch()
                .uri("/api/v1/branches/branch-1/name")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Centro Actualizado");
    }

    @Test
    void shouldDeleteBranch() {
        when(branchUseCase.deleteBranch(anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/branches/branch-1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
