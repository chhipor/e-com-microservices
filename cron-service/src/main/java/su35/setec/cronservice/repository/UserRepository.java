package su35.setec.cronservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import su35.setec.cronservice.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
}
