package su35.setec.operationservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import su35.setec.operationservice.entity.OrderStatus;

@Getter
@Setter
public class OrderUpdateRequest {

    @NotNull
    private OrderStatus status;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<OrderItemRequest> items;
}
