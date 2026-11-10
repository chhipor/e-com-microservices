package su35.setec.operationservice.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartResponse {

    private UUID id;
    private UUID customerId;
    private List<CartItemResponse> items;
    private Integer totalQuantity;
    private BigDecimal totalAmount;
    private Instant createdAt;
    private Instant updatedAt;
}
