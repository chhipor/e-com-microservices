package su35.setec.operationservice.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import su35.setec.operationservice.dto.response.CustomerResponse;
import su35.setec.operationservice.dto.response.OrderItemResponse;
import su35.setec.operationservice.dto.response.OrderResponse;
import su35.setec.operationservice.entity.Order;
import su35.setec.operationservice.entity.OrderItem;

@Component
public class OrderMapper {

    private final CustomerMapper customerMapper;

    public OrderMapper(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .customer(customerMapper.toResponse(order.getCustomer()))
                .items(order.getItems().stream().map(this::toItemResponse).collect(Collectors.toList()))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .lineTotal(item.getLineTotal())
                .build();
    }
}
