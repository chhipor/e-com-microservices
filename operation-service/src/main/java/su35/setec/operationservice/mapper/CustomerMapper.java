package su35.setec.operationservice.mapper;

import org.springframework.stereotype.Component;
import su35.setec.operationservice.dto.response.CustomerResponse;
import su35.setec.operationservice.entity.Customer;

@Component
public class CustomerMapper {

    public CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
