package su35.setec.operationservice.service;

import su35.setec.operationservice.dto.request.CustomerProfileRequest;
import su35.setec.operationservice.dto.response.CustomerResponse;
import su35.setec.operationservice.entity.Customer;

/** Resolves the customer record that belongs to the signed-in auth-service user. */
public interface CustomerAccountService {

    /** Finds the caller's customer, linking an existing record by email or creating one on first use. */
    Customer currentCustomer();

    CustomerResponse getProfile();

    CustomerResponse updateProfile(CustomerProfileRequest request);
}
