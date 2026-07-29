package su35.setec.operationservice.mapper;

import org.springframework.stereotype.Component;
import su35.setec.operationservice.dto.response.ProductResponse;
import su35.setec.operationservice.entity.Product;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .quantityInStock(product.getQuantityInStock())
                .active(product.isActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
