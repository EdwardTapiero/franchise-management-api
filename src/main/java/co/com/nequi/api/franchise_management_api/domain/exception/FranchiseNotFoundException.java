package co.com.nequi.api.franchise_management_api.domain.exception;

public class FranchiseNotFoundException extends DomainException {
    public FranchiseNotFoundException(String franchiseId) {
        super(String.format("Franchise with id '%s' not found", franchiseId));
    }
}