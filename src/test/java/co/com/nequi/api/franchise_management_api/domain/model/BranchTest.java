package co.com.nequi.api.franchise_management_api.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BranchTest {

    @Test
    void shouldCreateBranch() {
        Branch branch = Branch.builder()
                .id("1")
                .name("Centro")
                .franchiseId("franchise-1")
                .build();

        assertNotNull(branch);
        assertEquals("Centro", branch.getName());
        assertEquals("franchise-1", branch.getFranchiseId());
    }

    @Test
    void shouldAddProduct() {
        Branch branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .build();

        Product product = Product.builder()
                .id("product-1")
                .name("Big Mac")
                .stock(50)
                .build();

        branch.addProduct(product);

        assertEquals(1, branch.getProducts().size());
        assertEquals("branch-1", product.getBranchId());
    }

    @Test
    void shouldThrowExceptionWhenAddingNullProduct() {
        Branch branch = Branch.builder()
                .id("branch-1")
                .build();

        assertThrows(IllegalArgumentException.class, () -> branch.addProduct(null));
    }

    @Test
    void shouldRemoveProduct() {
        Branch branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .build();

        Product product = Product.builder()
                .id("product-1")
                .name("Big Mac")
                .stock(50)
                .build();

        branch.addProduct(product);
        branch.removeProduct("product-1");

        assertEquals(0, branch.getProducts().size());
    }

    @Test
    void shouldFindProductWithMaxStock() {
        Branch branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .build();

        Product product1 = Product.builder()
                .id("product-1")
                .name("Big Mac")
                .stock(50)
                .build();

        Product product2 = Product.builder()
                .id("product-2")
                .name("Papas")
                .stock(100)
                .build();

        branch.addProduct(product1);
        branch.addProduct(product2);

        Product maxStockProduct = branch.findProductWithMaxStock();

        assertNotNull(maxStockProduct);
        assertEquals("product-2", maxStockProduct.getId());
        assertEquals(100, maxStockProduct.getStock());
    }

    @Test
    void shouldReturnNullWhenNoProducts() {
        Branch branch = Branch.builder()
                .id("branch-1")
                .name("Centro")
                .build();

        Product maxStockProduct = branch.findProductWithMaxStock();

        assertNull(maxStockProduct);
    }

}
