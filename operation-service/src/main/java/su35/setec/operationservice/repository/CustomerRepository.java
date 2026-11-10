package su35.setec.operationservice.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import su35.setec.operationservice.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    boolean existsByEmail(String email);

    Optional<Customer> findByUserId(UUID userId);

    Optional<Customer> findByEmailIgnoreCase(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);
}
