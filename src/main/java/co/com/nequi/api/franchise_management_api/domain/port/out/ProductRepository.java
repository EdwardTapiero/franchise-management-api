package co.com.nequi.api.franchise_management_api.domain.port.out;

import co.com.nequi.api.franchise_management_api.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {

    Mono<Product> save(Product product);

    Mono<Product> findById(String id);

    Flux<Product> findByBranchId(String branchId);

    Flux<Product> findAll();

    Mono<Product> update(Product product);

    Mono<Void> deleteById(String id);

}
