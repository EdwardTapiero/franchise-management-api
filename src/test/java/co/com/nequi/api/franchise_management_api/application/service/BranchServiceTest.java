package co.com.nequi.api.franchise_management_api.application.service;

import co.com.nequi.api.franchise_management_api.domain.exception.BranchNotFoundException;
import co.com.nequi.api.franchise_management_api.domain.exception.FranchiseNotFoundException;
import co.com.nequi.api.franchise_management_api.domain.model.Branch;
import co.com.nequi.api.franchise_management_api.domain.model.Franchise;
import co.com.nequi.api.franchise_management_api.domain.port.out.BranchRepository;
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
public class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private BranchService branchService;

    private Branch branch;
    private Franchise franchise;

    @BeforeEach
    void setUp() {
        branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .franchiseId("franchise-1")
                .build();

        franchise = Franchise.builder()
                .id("franchise-1")
                .name("McDonalds")
                .build();
    }

    @Test
    void shouldAddBranchToFranchise() {
        when(franchiseRepository.findById(anyString())).thenReturn(Mono.just(franchise));
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.just(branch));

        Mono<Branch> result = branchService.addBranchToFranchise("franchise-1", branch);

        StepVerifier.create(result)
                .expectNextMatches(b -> b.getName().equals("Centro"))
                .verifyComplete();

        verify(franchiseRepository).findById("franchise-1");
        verify(branchRepository).save(any(Branch.class));
    }

    @Test
    void shouldThrowErrorWhenFranchiseNotFoundOnAdd() {
        when(franchiseRepository.findById(anyString())).thenReturn(Mono.empty());

        Mono<Branch> result = branchService.addBranchToFranchise("non-existent", branch);

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(franchiseRepository).findById("non-existent");
        verify(branchRepository, never()).save(any());
    }

    @Test
    void shouldGetBranchById() {
        when(branchRepository.findById(anyString())).thenReturn(Mono.just(branch));

        Mono<Branch> result = branchService.getBranchById("branch-1");

        StepVerifier.create(result)
                .expectNextMatches(b -> b.getId().equals("branch-1"))
                .verifyComplete();

        verify(branchRepository).findById("branch-1");
    }

    @Test
    void shouldThrowErrorWhenBranchNotFound() {
        when(branchRepository.findById(anyString())).thenReturn(Mono.empty());

        Mono<Branch> result = branchService.getBranchById("non-existent");

        StepVerifier.create(result)
                .expectError(BranchNotFoundException.class)
                .verify();

        verify(branchRepository).findById("non-existent");
    }

    @Test
    void shouldGetBranchesByFranchiseId() {
        when(franchiseRepository.findById(anyString())).thenReturn(Mono.just(franchise));
        when(branchRepository.findByFranchiseId(anyString())).thenReturn(Flux.just(branch));

        Flux<Branch> result = branchService.getBranchesByFranchiseId("franchise-1");

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();

        verify(franchiseRepository).findById("franchise-1");
        verify(branchRepository).findByFranchiseId("franchise-1");
    }

    @Test
    void shouldUpdateBranchName() {
        Branch updated = Branch.builder()
                .id("branch-1")
                .name("Centro Updated")
                .build();

        when(branchRepository.findById(anyString())).thenReturn(Mono.just(branch));
        when(branchRepository.update(any(Branch.class))).thenReturn(Mono.just(updated));

        Mono<Branch> result = branchService.updateBranchName("branch-1", "Centro Updated");

        StepVerifier.create(result)
                .expectNextMatches(b -> b.getName().equals("Centro Updated"))
                .verifyComplete();

        verify(branchRepository).findById("branch-1");
        verify(branchRepository).update(any(Branch.class));
    }

    @Test
    void shouldDeleteBranch() {
        when(branchRepository.findById(anyString())).thenReturn(Mono.just(branch));
        when(branchRepository.deleteById(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = branchService.deleteBranch("branch-1");

        StepVerifier.create(result)
                .verifyComplete();

        verify(branchRepository).findById("branch-1");
        verify(branchRepository).deleteById("branch-1");
    }

}
