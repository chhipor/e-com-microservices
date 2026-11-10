package su35.setec.operationservice.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemResponse {

    private UUID id;
    private UUID productId;
    private String productName;
    private String sku;
    private String thumbnail;
    /** Current product price; the price is locked in only at checkout. */
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal lineTotal;
    private Integer quantityInStock;
    /** False when the product is inactive or stock is below the requested quantity. */
    private boolean available;
    private Instant addedAt;
}
