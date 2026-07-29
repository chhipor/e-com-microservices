package su35.setec.operationservice.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import su35.setec.operationservice.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    boolean existsByOrderNumber(String orderNumber);

    @Override
    @EntityGraph(attributePaths = {"customer", "items", "items.product"})
    Optional<Order> findById(UUID id);
}
