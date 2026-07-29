package su35.setec.operationservice.service;

import java.util.List;
import java.util.UUID;
import su35.setec.operationservice.dto.request.CustomerRequest;
import su35.setec.operationservice.dto.response.CustomerResponse;

public interface CustomerService {

    CustomerResponse create(CustomerRequest request);

    List<CustomerResponse> findAll();

    CustomerResponse findById(UUID id);

    CustomerResponse update(UUID id, CustomerRequest request);

    void delete(UUID id);
}
