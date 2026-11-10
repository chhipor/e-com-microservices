package su35.setec.operationservice.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su35.setec.operationservice.dto.response.OrderResponse;
import su35.setec.operationservice.service.CustomerAccountService;
import su35.setec.operationservice.service.OrderService;

/** Orders that belong to the signed-in customer. */
@RestController
@RequestMapping("/orders/me")
@RequiredArgsConstructor
public class MyOrderController {

    private final OrderService orderService;
    private final CustomerAccountService customerAccountService;

    /** Newest first. */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> findMine() {
        return ResponseEntity.ok(orderService.findForCustomer(customerAccountService.currentCustomer().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findMine(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.findForCustomer(customerAccountService.currentCustomer().getId(), id));
    }

    /** Cancels one of the caller's PENDING orders and returns the stock. */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.cancelForCustomer(customerAccountService.currentCustomer().getId(), id));
    }
}
