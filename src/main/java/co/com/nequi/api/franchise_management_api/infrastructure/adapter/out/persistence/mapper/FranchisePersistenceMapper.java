package co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.mapper;

import co.com.nequi.api.franchise_management_api.domain.model.Franchise;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.entity.FranchiseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FranchisePersistenceMapper {

    public FranchiseEntity toEntity(Franchise franchise) {
        if (franchise == null) {
            return null;
        }

        return FranchiseEntity.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Franchise toDomain(FranchiseEntity entity) {
        if (entity == null) {
            return null;
        }

        return Franchise.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

}
