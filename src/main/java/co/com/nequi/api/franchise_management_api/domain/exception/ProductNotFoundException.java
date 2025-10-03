package co.com.nequi.api.franchise_management_api.domain.exception;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(String productId) {
        super(String.format("Product with id '%s' not found", productId));
    }
}