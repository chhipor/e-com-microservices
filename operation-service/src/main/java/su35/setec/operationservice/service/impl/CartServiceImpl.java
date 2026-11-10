package su35.setec.operationservice.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su35.setec.operationservice.dto.request.CartItemQuantityRequest;
import su35.setec.operationservice.dto.request.CartItemRequest;
import su35.setec.operationservice.dto.request.OrderItemRequest;
import su35.setec.operationservice.dto.response.CartResponse;
import su35.setec.operationservice.dto.response.OrderResponse;
import su35.setec.operationservice.entity.Cart;
import su35.setec.operationservice.entity.CartItem;
import su35.setec.operationservice.entity.Customer;
import su35.setec.operationservice.entity.OrderStatus;
import su35.setec.operationservice.entity.Product;
import su35.setec.operationservice.exception.BadRequestException;
import su35.setec.operationservice.exception.NotFoundException;
import su35.setec.operationservice.mapper.CartMapper;
import su35.setec.operationservice.repository.CartRepository;
import su35.setec.operationservice.repository.ProductRepository;
import su35.setec.operationservice.service.CartService;
import su35.setec.operationservice.service.CustomerAccountService;
import su35.setec.operationservice.service.OrderService;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final int MAX_QUANTITY_PER_ITEM = 99;

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CustomerAccountService customerAccountService;
    private final OrderService orderService;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public CartResponse getCart() {
        return cartMapper.toResponse(currentCart());
    }

    @Override
    @Transactional
    public CartResponse addItem(CartItemRequest request) {
        Cart cart = currentCart();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found: " + request.getProductId()));

        CartItem item = findItem(cart, product.getId());
        int newQuantity = (item == null ? 0 : item.getQuantity()) + request.getQuantity();
        validate(product, newQuantity);

        if (item == null) {
            cart.getItems().add(CartItem.builder().cart(cart).product(product).quantity(newQuantity).build());
        } else {
            item.setQuantity(newQuantity);
        }
        return save(cart);
    }

    @Override
    @Transactional
    public CartResponse updateItem(UUID productId, CartItemQuantityRequest request) {
        Cart cart = currentCart();
        CartItem item = requireItem(cart, productId);
        validate(item.getProduct(), request.getQuantity());
        item.setQuantity(request.getQuantity());
        return save(cart);
    }

    @Override
    @Transactional
    public CartResponse removeItem(UUID productId) {
        Cart cart = currentCart();
        cart.getItems().remove(requireItem(cart, productId));
        return save(cart);
    }

    @Override
    @Transactional
    public CartResponse clear() {
        Cart cart = currentCart();
        cart.getItems().clear();
        return save(cart);
    }

    @Override
    @Transactional
    public OrderResponse checkout() {
        Cart cart = currentCart();
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        List<OrderItemRequest> items = cart.getItems().stream().map(cartItem -> {
            OrderItemRequest orderItem = new OrderItemRequest();
            orderItem.setProductId(cartItem.getProduct().getId());
            orderItem.setQuantity(cartItem.getQuantity());
            return orderItem;
        }).toList();

        // No payment integration yet: orders start PENDING until an admin completes them.
        // Stock/active checks happen inside placeOrder; a failure rolls back and keeps the cart.
        OrderResponse order = orderService.placeOrder(cart.getCustomer(), items, OrderStatus.PENDING);
        cart.getItems().clear();
        save(cart);
        return order;
    }

    private Cart currentCart() {
        Customer customer = customerAccountService.currentCustomer();
        return cartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> cartRepository.saveAndFlush(Cart.builder().customer(customer).build()));
    }

    private CartResponse save(Cart cart) {
        // Item changes do not dirty the cart row itself, so bump updatedAt explicitly.
        cart.setUpdatedAt(Instant.now());
        return cartMapper.toResponse(cartRepository.saveAndFlush(cart));
    }

    private static void validate(Product product, int quantity) {
        if (!product.isActive()) {
            throw new BadRequestException("Product is inactive: " + product.getName());
        }
        if (quantity > MAX_QUANTITY_PER_ITEM) {
            throw new BadRequestException("Maximum quantity per product is " + MAX_QUANTITY_PER_ITEM);
        }
        if (product.getQuantityInStock() < quantity) {
            throw new BadRequestException("Insufficient stock for product: " + product.getName()
                    + " (available: " + product.getQuantityInStock() + ")");
        }
    }

    private static CartItem findItem(Cart cart, UUID productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private static CartItem requireItem(Cart cart, UUID productId) {
        CartItem item = findItem(cart, productId);
        if (item == null) {
            throw new NotFoundException("Product is not in the cart: " + productId);
        }
        return item;
    }
}
