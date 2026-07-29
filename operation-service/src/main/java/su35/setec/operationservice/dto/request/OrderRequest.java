package su35.setec.operationservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

    @NotNull
    private UUID customerId;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<OrderItemRequest> items;
}
