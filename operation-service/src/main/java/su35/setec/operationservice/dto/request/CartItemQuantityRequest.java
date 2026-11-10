package su35.setec.operationservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemQuantityRequest {

    @NotNull
    @Min(1)
    @Max(99)
    private Integer quantity;
}
