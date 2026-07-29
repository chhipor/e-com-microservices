package su35.setec.operationservice.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import su35.setec.operationservice.entity.OrderStatus;

@Getter
@Setter
@Builder
public class OrderResponse {

    private UUID id;
    private String orderNumber;
    private Instant orderDate;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private CustomerResponse customer;
    private List<OrderItemResponse> items;
    private Instant createdAt;
    private Instant updatedAt;
}
