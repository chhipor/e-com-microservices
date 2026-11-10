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
public class ProductResponse {

    private UUID id;
    private String name;
    private String sku;
    private String description;
    private BigDecimal unitPrice;
    private Integer quantityInStock;
    private boolean active;
    private String thumbnail;
    private List<String> images;
    private Instant createdAt;
    private Instant updatedAt;
}
