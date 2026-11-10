package su35.setec.operationservice.service;

import su35.setec.operationservice.dto.request.OrderItemRequest;
import su35.setec.operationservice.entity.Customer;
import su35.setec.operationservice.entity.OrderStatus;

import java.util.List;
import java.util.UUID;
import su35.setec.operationservice.dto.request.OrderRequest;
import su35.setec.operationservice.dto.request.OrderUpdateRequest;
import su35.setec.operationservice.dto.response.OrderResponse;

public interface OrderService {

    OrderResponse create(OrderRequest request);

    List<OrderResponse> findAll();

    OrderResponse findById(UUID id);

    OrderResponse update(UUID id, OrderUpdateRequest request);

    void delete(UUID id);

    /** Creates an order for an existing customer, deducting stock. Used by cart checkout. */
    OrderResponse placeOrder(Customer customer, List<OrderItemRequest> items, OrderStatus status);

    List<OrderResponse> findForCustomer(UUID customerId);

    OrderResponse findForCustomer(UUID customerId, UUID orderId);

    /** Cancels a PENDING order owned by the customer and returns its stock. */
    OrderResponse cancelForCustomer(UUID customerId, UUID orderId);
}
