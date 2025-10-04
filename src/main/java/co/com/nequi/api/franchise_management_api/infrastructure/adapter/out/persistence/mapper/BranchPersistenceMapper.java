package co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.mapper;

import co.com.nequi.api.franchise_management_api.domain.model.Branch;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.entity.BranchEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BranchPersistenceMapper {

    public BranchEntity toEntity(Branch branch) {
        if (branch == null) {
            return null;
        }

        return BranchEntity.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Branch toDomain(BranchEntity entity) {
        if (entity == null) {
            return null;
        }

        return Branch.builder()
                .id(entity.getId())
                .name(entity.getName())
                .franchiseId(entity.getFranchiseId())
                .build();
    }

}
