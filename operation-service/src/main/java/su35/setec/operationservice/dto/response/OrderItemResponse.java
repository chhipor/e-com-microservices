package su35.setec.operationservice.dto.response;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderItemResponse {

    private UUID id;
    private UUID productId;
    private String productName;
    private String thumbnail;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}
