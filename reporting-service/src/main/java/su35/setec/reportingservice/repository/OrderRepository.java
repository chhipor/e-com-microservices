package su35.setec.reportingservice.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import su35.setec.reportingservice.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Override
    @EntityGraph(attributePaths = {"customer", "items", "items.product"})
    java.util.List<Order> findAll();

    @Override
    @EntityGraph(attributePaths = {"customer", "items", "items.product"})
    Optional<Order> findById(UUID id);
}
