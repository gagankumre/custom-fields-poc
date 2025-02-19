package in.clear.http.repository;

import in.clear.http.model.audit.AuditEventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditEventTypeRepository extends JpaRepository<AuditEventType, Long> {
}
