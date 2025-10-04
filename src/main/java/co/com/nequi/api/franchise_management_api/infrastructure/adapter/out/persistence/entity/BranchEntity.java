package co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "branches")
public class BranchEntity {

    @Id
    private String id;

    private String name;

    @Indexed
    private String franchiseId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
