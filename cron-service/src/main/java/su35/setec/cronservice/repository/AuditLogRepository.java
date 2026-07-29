package su35.setec.cronservice.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import su35.setec.cronservice.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    List<AuditLog> findAllByCreatedAtBeforeAndArchivedFalse(Instant createdAtBefore);
}
