package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.mapper;

import co.com.nequi.api.franchise_management_api.domain.model.Franchise;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.CreateFranchiseRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.FranchiseResponse;
import org.springframework.stereotype.Component;

@Component
public class FranchiseRestMapper {

    public Franchise toDomain(CreateFranchiseRequest request) {
        if (request == null) {
            return null;
        }

        return Franchise.builder()
                .name(request.getName())
                .build();
    }

    public FranchiseResponse toResponse(Franchise franchise) {
        if (franchise == null) {
            return null;
        }

        return FranchiseResponse.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }

}
