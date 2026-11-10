package su35.setec.operationservice.mapper;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;
import su35.setec.operationservice.dto.response.CartItemResponse;
import su35.setec.operationservice.dto.response.CartResponse;
import su35.setec.operationservice.entity.Cart;
import su35.setec.operationservice.entity.CartItem;
import su35.setec.operationservice.entity.Product;

@Component
public class CartMapper {

    public CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream().map(this::toItemResponse).toList();
        return CartResponse.builder()
                .id(cart.getId())
                .customerId(cart.getCustomer().getId())
                .items(items)
                .totalQuantity(items.stream().mapToInt(CartItemResponse::getQuantity).sum())
                .totalAmount(items.stream().map(CartItemResponse::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add))
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    private CartItemResponse toItemResponse(CartItem item) {
        Product product = item.getProduct();
        return CartItemResponse.builder()
                .id(item.getId())
                .productId(product.getId())
                .productName(product.getName())
                .sku(product.getSku())
                .thumbnail(product.getThumbnail())
                .unitPrice(product.getUnitPrice())
                .quantity(item.getQuantity())
                .lineTotal(product.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .quantityInStock(product.getQuantityInStock())
                .available(product.isActive() && product.getQuantityInStock() >= item.getQuantity())
                .addedAt(item.getCreatedAt())
                .build();
    }
}
