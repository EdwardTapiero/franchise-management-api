package co.com.nequi.api.franchise_management_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

    private String id;
    private String name;
    private String franchiseId;

    @Builder.Default
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        product.setBranchId(this.id);
        this.products.add(product);
    }

    public void removeProduct(String productId) {
        this.products.removeIf(p -> p.getId().equals(productId));
    }

    public Product findProductWithMaxStock() {
        return products.stream()
                .max(Comparator.comparingInt(Product::getStock))
                .orElse(null);
    }

}
