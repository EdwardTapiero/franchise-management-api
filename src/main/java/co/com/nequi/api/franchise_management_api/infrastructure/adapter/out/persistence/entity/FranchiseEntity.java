package co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "franchises")
public class FranchiseEntity {

    @Id
    private String id;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
