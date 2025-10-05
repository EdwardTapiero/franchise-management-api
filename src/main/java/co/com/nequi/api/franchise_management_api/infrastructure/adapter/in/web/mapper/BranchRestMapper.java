package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.mapper;

import co.com.nequi.api.franchise_management_api.domain.model.Branch;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.CreateBranchRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.BranchResponse;
import org.springframework.stereotype.Component;

@Component
public class BranchRestMapper {

    public Branch toDomain(CreateBranchRequest request) {
        if (request == null) {
            return null;
        }

        return Branch.builder()
                .name(request.getName())
                .build();
    }

    public BranchResponse toResponse(Branch branch) {
        if (branch == null) {
            return null;
        }

        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();
    }

}
