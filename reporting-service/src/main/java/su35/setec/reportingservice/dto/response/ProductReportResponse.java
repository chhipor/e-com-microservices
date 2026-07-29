package su35.setec.reportingservice.dto.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductReportResponse {

    private long totalProducts;
    private long activeProducts;
    private long totalStockQuantity;
    private BigDecimal averagePrice;
}
