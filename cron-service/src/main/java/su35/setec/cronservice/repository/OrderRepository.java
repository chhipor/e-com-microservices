package su35.setec.cronservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import su35.setec.cronservice.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Override
    @EntityGraph(attributePaths = {"items", "items.product", "customer"})
    java.util.List<Order> findAll();
}
