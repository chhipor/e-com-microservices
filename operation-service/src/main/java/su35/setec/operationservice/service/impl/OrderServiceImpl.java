package su35.setec.operationservice.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su35.setec.operationservice.dto.request.OrderItemRequest;
import su35.setec.operationservice.dto.request.OrderRequest;
import su35.setec.operationservice.dto.request.OrderUpdateRequest;
import su35.setec.operationservice.dto.response.OrderResponse;
import su35.setec.operationservice.entity.Customer;
import su35.setec.operationservice.entity.Order;
import su35.setec.operationservice.entity.OrderItem;
import su35.setec.operationservice.entity.OrderStatus;
import su35.setec.operationservice.entity.Product;
import su35.setec.operationservice.exception.BadRequestException;
import su35.setec.operationservice.exception.NotFoundException;
import su35.setec.operationservice.mapper.OrderMapper;
import su35.setec.operationservice.repository.CustomerRepository;
import su35.setec.operationservice.repository.OrderRepository;
import su35.setec.operationservice.repository.ProductRepository;
import su35.setec.operationservice.service.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse create(OrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found: " + request.getCustomerId()));
        return placeOrder(customer, request.getItems(), OrderStatus.COMPLETED);
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(Customer customer, List<OrderItemRequest> items, OrderStatus status) {
        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .orderDate(Instant.now())
                .status(status)
                .customer(customer)
                .items(new ArrayList<>())
                .build();

        applyItems(order, items, status != OrderStatus.CANCELLED);
        recalculateTotal(order);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findForCustomer(UUID customerId) {
        return orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId).stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findForCustomer(UUID customerId, UUID orderId) {
        return orderMapper.toResponse(findOwnedOrder(customerId, orderId));
    }

    @Override
    @Transactional
    public OrderResponse cancelForCustomer(UUID customerId, UUID orderId) {
        Order order = findOwnedOrder(customerId, orderId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Only PENDING orders can be cancelled");
        }
        restoreStock(order);
        order.setStatus(OrderStatus.CANCELLED);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    private Order findOwnedOrder(UUID customerId, UUID orderId) {
        // Another customer's order is reported as not found so ids cannot be probed.
        return orderRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream().map(orderMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findById(UUID id) {
        return orderMapper.toResponse(orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id)));
    }

    @Override
    @Transactional
    public OrderResponse update(UUID id, OrderUpdateRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));

        // Cancelled orders already returned their stock; only active orders hold stock.
        if (order.getStatus() != OrderStatus.CANCELLED) {
            restoreStock(order);
        }
        order.getItems().clear();
        applyItems(order, request.getItems(), request.getStatus() != OrderStatus.CANCELLED);
        order.setStatus(request.getStatus());
        recalculateTotal(order);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));
        if (order.getStatus() != OrderStatus.CANCELLED) {
            restoreStock(order);
        }
        orderRepository.delete(order);
    }

    private void applyItems(Order order, List<OrderItemRequest> requests, boolean deductStock) {
        for (OrderItemRequest itemRequest : requests) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + itemRequest.getProductId()));

            if (!product.isActive()) {
                throw new BadRequestException("Product is inactive: " + product.getName());
            }

            int quantity = itemRequest.getQuantity();
            if (deductStock) {
                if (product.getQuantityInStock() < quantity) {
                    throw new BadRequestException("Insufficient stock for product: " + product.getName());
                }
                product.setQuantityInStock(product.getQuantityInStock() - quantity);
                productRepository.save(product);
            }

            BigDecimal unitPrice = product.getUnitPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .lineTotal(lineTotal)
                    .build();
            order.getItems().add(orderItem);
        }
    }

    private void restoreStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setQuantityInStock(product.getQuantityInStock() + item.getQuantity());
            productRepository.save(product);
        }
    }

    private void recalculateTotal(Order order) {
        order.setTotalAmount(order.getItems().stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private String generateOrderNumber() {
        String candidate = "ORD-" + Instant.now().toEpochMilli();
        while (orderRepository.existsByOrderNumber(candidate)) {
            candidate = candidate + "-" + UUID.randomUUID().toString().substring(0, 8);
        }
        return candidate;
    }
}
