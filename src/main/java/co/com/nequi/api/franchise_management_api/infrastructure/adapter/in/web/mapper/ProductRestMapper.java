package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.mapper;

import co.com.nequi.api.franchise_management_api.domain.model.Branch;
import co.com.nequi.api.franchise_management_api.domain.model.Product;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.CreateProductRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.MaxStockProductResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.ProductResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProductRestMapper {

    public Product toDomain(CreateProductRequest request) {
        if (request == null) {
            return null;
        }

        return Product.builder()
                .name(request.getName())
                .stock(request.getStock())
                .build();
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .build();
    }

    public Mono<MaxStockProductResponse> toMaxStockResponse(Product product, Mono<Branch> branchMono) {
        if (product == null) {
            return Mono.empty();
        }

        return branchMono
                .map(branch -> MaxStockProductResponse.builder()
                        .productId(product.getId())
                        .productName(product.getName())
                        .stock(product.getStock())
                        .branchId(branch.getId())
                        .branchName(branch.getName())
                        .build());
    }

}
