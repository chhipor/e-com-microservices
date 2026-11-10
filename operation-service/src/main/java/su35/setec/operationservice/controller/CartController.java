package su35.setec.operationservice.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su35.setec.operationservice.dto.request.CartItemQuantityRequest;
import su35.setec.operationservice.dto.request.CartItemRequest;
import su35.setec.operationservice.dto.response.CartResponse;
import su35.setec.operationservice.dto.response.OrderResponse;
import su35.setec.operationservice.service.CartService;

/** The signed-in user's shopping cart. Any authenticated user may use it. */
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> get() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @DeleteMapping
    public ResponseEntity<CartResponse> clear() {
        return ResponseEntity.ok(cartService.clear());
    }

    /** Adds a product, or increases its quantity if it is already in the cart. */
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(request));
    }

    /** Sets the quantity of a product that is already in the cart. */
    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateItem(@PathVariable UUID productId,
                                                   @Valid @RequestBody CartItemQuantityRequest request) {
        return ResponseEntity.ok(cartService.updateItem(productId, request));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable UUID productId) {
        return ResponseEntity.ok(cartService.removeItem(productId));
    }

    /** Turns the cart into a PENDING order (no payment yet) and empties the cart. */
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout() {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.checkout());
    }
}
