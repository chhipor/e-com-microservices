package su35.setec.operationservice.service;

import java.util.UUID;
import su35.setec.operationservice.dto.request.CartItemQuantityRequest;
import su35.setec.operationservice.dto.request.CartItemRequest;
import su35.setec.operationservice.dto.response.CartResponse;
import su35.setec.operationservice.dto.response.OrderResponse;

public interface CartService {

    CartResponse getCart();

    CartResponse addItem(CartItemRequest request);

    CartResponse updateItem(UUID productId, CartItemQuantityRequest request);

    CartResponse removeItem(UUID productId);

    CartResponse clear();

    OrderResponse checkout();
}
