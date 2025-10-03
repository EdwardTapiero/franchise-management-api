package co.com.nequi.api.franchise_management_api.domain.exception;

public class BranchNotFoundException extends DomainException {
    public BranchNotFoundException(String branchId) {
        super(String.format("Branch with id '%s' not found", branchId));
    }
}