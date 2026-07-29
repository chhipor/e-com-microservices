package su35.setec.operationservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import su35.setec.operationservice.validation.PositiveMoney;

@Getter
@Setter
public class ProductRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    @NotBlank
    @Size(max = 80)
    private String sku;

    @Size(max = 500)
    private String description;

    @NotNull
    @PositiveMoney
    private BigDecimal unitPrice;

    @NotNull
    @PositiveOrZero
    private Integer quantityInStock;

    private boolean active = true;
}
