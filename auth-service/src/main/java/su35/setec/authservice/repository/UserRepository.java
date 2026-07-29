package su35.setec.authservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import su35.setec.authservice.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<User> findByEmail(String email);

    @Override
    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    List<User> findAll();

    @Override
    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<User> findById(UUID uuid);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);
}
