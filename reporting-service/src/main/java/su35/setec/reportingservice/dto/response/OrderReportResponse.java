package su35.setec.reportingservice.dto.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderReportResponse {

    private long totalOrders;
    private long completedOrders;
    private long cancelledOrders;
    private BigDecimal totalSales;
    private BigDecimal averageOrderValue;
}
