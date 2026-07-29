package su35.setec.reportingservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import su35.setec.reportingservice.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
