package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.controller;

import co.com.nequi.api.franchise_management_api.domain.model.Franchise;
import co.com.nequi.api.franchise_management_api.domain.port.in.FranchiseUseCase;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.CreateFranchiseRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.FranchiseResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.mapper.FranchiseRestMapper;
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

@WebFluxTest(FranchiseController.class)
public class FranchiseControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private FranchiseUseCase franchiseUseCase;

    @MockitoBean
    private FranchiseRestMapper mapper;

    @Test
    void shouldCreateFranchise() {
        CreateFranchiseRequest request = CreateFranchiseRequest.builder()
                .name("McDonalds")
                .build();

        Franchise franchise = Franchise.builder()
                .id("franchise-1")
                .name("McDonalds")
                .build();

        FranchiseResponse response = FranchiseResponse.builder()
                .id("franchise-1")
                .name("McDonalds")
                .build();

        when(mapper.toDomain(any())).thenReturn(franchise);
        when(franchiseUseCase.createFranchise(any())).thenReturn(Mono.just(franchise));
        when(mapper.toResponse(any())).thenReturn(response);

        webTestClient.post()
                .uri("/api/v1/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("franchise-1")
                .jsonPath("$.name").isEqualTo("McDonalds");
    }

    @Test
    void shouldGetFranchiseById() {
        Franchise franchise = Franchise.builder()
                .id("franchise-1")
                .name("McDonalds")
                .build();

        FranchiseResponse response = FranchiseResponse.builder()
                .id("franchise-1")
                .name("McDonalds")
                .build();

        when(franchiseUseCase.getFranchiseById(anyString())).thenReturn(Mono.just(franchise));
        when(mapper.toResponse(any())).thenReturn(response);

        webTestClient.get()
                .uri("/api/v1/franchises/franchise-1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("franchise-1")
                .jsonPath("$.name").isEqualTo("McDonalds");
    }

    @Test
    void shouldGetAllFranchises() {
        Franchise franchise = Franchise.builder()
                .id("franchise-1")
                .name("McDonalds")
                .build();

        FranchiseResponse response = FranchiseResponse.builder()
                .id("franchise-1")
                .name("McDonalds")
                .build();

        when(franchiseUseCase.getAllFranchises()).thenReturn(Flux.just(franchise));
        when(mapper.toResponse(any())).thenReturn(response);

        webTestClient.get()
                .uri("/api/v1/franchises")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FranchiseResponse.class)
                .hasSize(1);
    }

    @Test
    void shouldReturnBadRequestWhenInvalidInput() {
        CreateFranchiseRequest request = CreateFranchiseRequest.builder()
                .name("A") // Menos de 2 caracteres
                .build();

        webTestClient.post()
                .uri("/api/v1/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
