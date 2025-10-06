package co.com.nequi.api.franchise_management_api.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    void shouldCreateProduct() {
        Product product = Product.builder()
                .id("1")
                .name("Big Mac")
                .stock(50)
                .branchId("branch-1")
                .build();

        assertNotNull(product);
        assertEquals("Big Mac", product.getName());
        assertEquals(50, product.getStock());
    }

    @Test
    void shouldUpdateStock() {
        Product product = Product.builder()
                .stock(50)
                .build();

        product.updateStock(100);

        assertEquals(100, product.getStock());
    }

    @Test
    void shouldThrowExceptionWhenNegativeStock() {
        Product product = Product.builder()
                .stock(50)
                .build();

        assertThrows(IllegalArgumentException.class, () -> product.updateStock(-10));
    }

    @Test
    void shouldIncrementStock() {
        Product product = Product.builder()
                .stock(50)
                .build();

        product.incrementStock(25);

        assertEquals(75, product.getStock());
    }

    @Test
    void shouldDecrementStock() {
        Product product = Product.builder()
                .stock(50)
                .build();

        product.decrementStock(20);

        assertEquals(30, product.getStock());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientStock() {
        Product product = Product.builder()
                .stock(10)
                .build();

        assertThrows(IllegalArgumentException.class, () -> product.decrementStock(20));
    }

    @Test
    void shouldThrowExceptionWhenNegativeIncrement() {
        Product product = Product.builder()
                .stock(50)
                .build();

        assertThrows(IllegalArgumentException.class, () -> product.incrementStock(-10));
    }

    @Test
    void shouldThrowExceptionWhenNegativeDecrement() {
        Product product = Product.builder()
                .stock(50)
                .build();

        assertThrows(IllegalArgumentException.class, () -> product.decrementStock(-10));
    }

}
