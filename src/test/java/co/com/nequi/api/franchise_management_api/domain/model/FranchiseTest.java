package co.com.nequi.api.franchise_management_api.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FranchiseTest {

    @Test
    void shouldCreateFranchise() {
        Franchise franchise = Franchise.builder()
                .id("1")
                .name("McDonalds")
                .build();

        assertNotNull(franchise);
        assertEquals("McDonalds", franchise.getName());
    }

    @Test
    void shouldAddBranch() {
        Franchise franchise = Franchise.builder()
                .id("franchise-1")
                .name("McDonalds")
                .build();

        Branch branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .build();

        franchise.addBranch(branch);

        assertEquals(1, franchise.getBranches().size());
        assertEquals("franchise-1", branch.getFranchiseId());
    }

    @Test
    void shouldThrowExceptionWhenAddingNullBranch() {
        Franchise franchise = Franchise.builder()
                .id("franchise-1")
                .build();

        assertThrows(IllegalArgumentException.class, () -> franchise.addBranch(null));
    }

    @Test
    void shouldRemoveBranch() {
        Franchise franchise = Franchise.builder()
                .id("franchise-1")
                .name("McDonalds")
                .build();

        Branch branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .build();

        franchise.addBranch(branch);
        franchise.removeBranch("branch-1");

        assertEquals(0, franchise.getBranches().size());
    }

    @Test
    void shouldFindBranchById() {
        Franchise franchise = Franchise.builder()
                .id("franchise-1")
                .name("McDonalds")
                .build();

        Branch branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .build();

        franchise.addBranch(branch);

        Branch found = franchise.findBranchById("branch-1");

        assertNotNull(found);
        assertEquals("branch-1", found.getId());
    }

    @Test
    void shouldReturnNullWhenBranchNotFound() {
        Franchise franchise = Franchise.builder()
                .id("franchise-1")
                .name("McDonalds")
                .build();

        Branch found = franchise.findBranchById("non-existent");

        assertNull(found);
    }

}
