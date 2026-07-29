package su35.setec.cronservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import su35.setec.cronservice.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
