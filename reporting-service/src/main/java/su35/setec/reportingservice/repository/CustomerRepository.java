package su35.setec.reportingservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import su35.setec.reportingservice.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
