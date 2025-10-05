package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaxStockProductResponse {
    private String productId;
    private String productName;
    private Integer stock;
    private String branchId;
    private String branchName;
}
