package co.com.nequi.api.franchise_management_api.domain.port.in;

import co.com.nequi.api.franchise_management_api.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductUseCase {

    Mono<Product> addProductToBranch(String branchId, Product product);

    Mono<Product> getProductById(String id);

    Flux<Product> getProductsByBranchId(String branchId);

    Mono<Product> updateProductStock(String id, Integer newStock);

    Mono<Product> updateProductName(String id, String newName);

    Mono<Void> deleteProduct(String id);

    Flux<Product> getProductsWithMaxStockByFranchise(String franchiseId);

}
