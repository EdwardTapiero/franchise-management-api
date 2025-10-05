package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFranchiseRequest {

    @NotBlank(message = "Franchise name is required")
    @Size(min = 2, max = 100, message = "Franchise name must be between 2 and 100 characters")
    private String name;

}
