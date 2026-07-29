package su35.setec.operationservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import su35.setec.operationservice.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsBySku(String sku);

    boolean existsByName(String name);

    boolean existsBySkuAndIdNot(String sku, UUID id);

    boolean existsByNameAndIdNot(String name, UUID id);
}
