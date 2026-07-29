package su35.setec.operationservice.service;

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
}
