package su35.setec.operationservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su35.setec.operationservice.dto.request.CustomerProfileRequest;
import su35.setec.operationservice.dto.response.CustomerResponse;
import su35.setec.operationservice.service.CustomerAccountService;

/** The signed-in user's own customer profile (created automatically on first use). */
@RestController
@RequestMapping("/customers/me")
@RequiredArgsConstructor
public class CustomerAccountController {

    private final CustomerAccountService customerAccountService;

    @GetMapping
    public ResponseEntity<CustomerResponse> get() {
        return ResponseEntity.ok(customerAccountService.getProfile());
    }

    @PutMapping
    public ResponseEntity<CustomerResponse> update(@Valid @RequestBody CustomerProfileRequest request) {
        return ResponseEntity.ok(customerAccountService.updateProfile(request));
    }
}
