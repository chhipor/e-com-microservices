package su35.setec.reportingservice.dto.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SalesReportResponse {

    private BigDecimal totalRevenue;
    private long totalOrders;
    private String bestSellingProduct;
    private long bestSellingQuantity;
}
