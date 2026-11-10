package su35.setec.operationservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su35.setec.operationservice.dto.request.CustomerProfileRequest;
import su35.setec.operationservice.dto.response.CustomerResponse;
import su35.setec.operationservice.entity.Customer;
import su35.setec.operationservice.exception.ApiException;
import su35.setec.operationservice.mapper.CustomerMapper;
import su35.setec.operationservice.repository.CustomerRepository;
import su35.setec.operationservice.security.AuthenticatedUser;
import su35.setec.operationservice.security.CurrentUser;
import su35.setec.operationservice.service.CustomerAccountService;

@Service
@RequiredArgsConstructor
public class CustomerAccountServiceImpl implements CustomerAccountService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CurrentUser currentUser;

    @Override
    @Transactional
    public Customer currentCustomer() {
        AuthenticatedUser user = currentUser.get();
        if (user.userId() == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Access token has no userId claim");
        }

        return customerRepository.findByUserId(user.userId())
                .orElseGet(() -> customerRepository.findByEmailIgnoreCase(user.email())
                        .map(existing -> linkExisting(existing, user))
                        .orElseGet(() -> customerRepository.save(newCustomer(user))));
    }

    @Override
    @Transactional
    public CustomerResponse getProfile() {
        return customerMapper.toResponse(currentCustomer());
    }

    @Override
    @Transactional
    public CustomerResponse updateProfile(CustomerProfileRequest request) {
        Customer customer = currentCustomer();
        customer.setFirstName(request.getFirstName().trim());
        customer.setLastName(request.getLastName().trim());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        return customerMapper.toResponse(customerRepository.saveAndFlush(customer));
    }

    /** Links an admin-created customer with the same email to the signed-in user on first use. */
    private Customer linkExisting(Customer existing, AuthenticatedUser user) {
        if (existing.getUserId() != null) {
            throw new ApiException(HttpStatus.CONFLICT, "Customer email is linked to another account");
        }
        existing.setUserId(user.userId());
        return customerRepository.save(existing);
    }

    private static Customer newCustomer(AuthenticatedUser user) {
        String email = user.email();
        int at = email.indexOf('@');
        String fallbackFirstName = at > 0 ? email.substring(0, at) : email;
        return Customer.builder()
                .userId(user.userId())
                .email(email)
                .firstName(isBlank(user.firstName()) ? fallbackFirstName : user.firstName())
                .lastName(isBlank(user.lastName()) ? "" : user.lastName())
                .build();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
