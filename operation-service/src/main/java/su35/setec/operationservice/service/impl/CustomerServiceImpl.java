package su35.setec.operationservice.service.impl;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su35.setec.operationservice.dto.request.CustomerRequest;
import su35.setec.operationservice.dto.response.CustomerResponse;
import su35.setec.operationservice.entity.Customer;
import su35.setec.operationservice.exception.BadRequestException;
import su35.setec.operationservice.exception.NotFoundException;
import su35.setec.operationservice.mapper.CustomerMapper;
import su35.setec.operationservice.repository.CustomerRepository;
import su35.setec.operationservice.service.CustomerService;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream().map(customerMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse findById(UUID id) {
        return customerMapper.toResponse(customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + id)));
    }

    @Override
    @Transactional
    public CustomerResponse update(UUID id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + id));

        if (!customer.getEmail().equals(request.getEmail()) && customerRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new BadRequestException("Email already exists");
        }

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new NotFoundException("Customer not found: " + id);
        }
        customerRepository.deleteById(id);
    }
}
