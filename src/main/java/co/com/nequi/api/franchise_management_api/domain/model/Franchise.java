package co.com.nequi.api.franchise_management_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Franchise {

    private String id;
    private String name;

    @Builder.Default
    private List<Branch> branches = new ArrayList<>();

    public void addBranch(Branch branch) {
        if (branch == null) {
            throw new IllegalArgumentException("Branch cannot be null");
        }
        branch.setFranchiseId(this.id);
        this.branches.add(branch);
    }

    public void removeBranch(String branchId) {
        this.branches.removeIf(b -> b.getId().equals(branchId));
    }

    public Branch findBranchById(String branchId) {
        return branches.stream()
                .filter(b -> b.getId().equals(branchId))
                .findFirst()
                .orElse(null);
    }
}
