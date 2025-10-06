package co.com.nequi.api.franchise_management_api.application.service;

import co.com.nequi.api.franchise_management_api.domain.exception.FranchiseNotFoundException;
import co.com.nequi.api.franchise_management_api.domain.model.Franchise;
import co.com.nequi.api.franchise_management_api.domain.port.out.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FranchiseServiceTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private FranchiseService franchiseService;

    private Franchise franchise;

    @BeforeEach
    void setUp() {
        franchise = Franchise.builder()
                .id("franchise-1")
                .name("McDonalds")
                .build();
    }

    @Test
    void shouldCreateFranchise() {
        when(franchiseRepository.existsByName(anyString())).thenReturn(Mono.just(false));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        Mono<Franchise> result = franchiseService.createFranchise(franchise);

        StepVerifier.create(result)
                .expectNextMatches(f -> f.getName().equals("McDonalds"))
                .verifyComplete();

        verify(franchiseRepository).existsByName("McDonalds");
        verify(franchiseRepository).save(any(Franchise.class));
    }

    @Test
    void shouldThrowErrorWhenFranchiseAlreadyExists() {
        when(franchiseRepository.existsByName(anyString())).thenReturn(Mono.just(true));

        Mono<Franchise> result = franchiseService.createFranchise(franchise);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("already exists"))
                .verify();

        verify(franchiseRepository).existsByName("McDonalds");
        verify(franchiseRepository, never()).save(any());
    }

    @Test
    void shouldGetFranchiseById() {
        when(franchiseRepository.findById(anyString())).thenReturn(Mono.just(franchise));

        Mono<Franchise> result = franchiseService.getFranchiseById("franchise-1");

        StepVerifier.create(result)
                .expectNextMatches(f -> f.getId().equals("franchise-1"))
                .verifyComplete();

        verify(franchiseRepository).findById("franchise-1");
    }

    @Test
    void shouldThrowErrorWhenFranchiseNotFound() {
        when(franchiseRepository.findById(anyString())).thenReturn(Mono.empty());

        Mono<Franchise> result = franchiseService.getFranchiseById("non-existent");

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(franchiseRepository).findById("non-existent");
    }

    @Test
    void shouldGetAllFranchises() {
        when(franchiseRepository.findAll()).thenReturn(Flux.just(franchise));

        Flux<Franchise> result = franchiseService.getAllFranchises();

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();

        verify(franchiseRepository).findAll();
    }

    @Test
    void shouldUpdateFranchiseName() {
        Franchise updated = Franchise.builder()
                .id("franchise-1")
                .name("McDonalds Updated")
                .build();

        when(franchiseRepository.findById(anyString())).thenReturn(Mono.just(franchise));
        when(franchiseRepository.update(any(Franchise.class))).thenReturn(Mono.just(updated));

        Mono<Franchise> result = franchiseService.updateFranchiseName("franchise-1", "McDonalds Updated");

        StepVerifier.create(result)
                .expectNextMatches(f -> f.getName().equals("McDonalds Updated"))
                .verifyComplete();

        verify(franchiseRepository).findById("franchise-1");
        verify(franchiseRepository).update(any(Franchise.class));
    }

    @Test
    void shouldDeleteFranchise() {
        when(franchiseRepository.findById(anyString())).thenReturn(Mono.just(franchise));
        when(franchiseRepository.deleteById(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = franchiseService.deleteFranchise("franchise-1");

        StepVerifier.create(result)
                .verifyComplete();

        verify(franchiseRepository).findById("franchise-1");
        verify(franchiseRepository).deleteById("franchise-1");
    }
}
