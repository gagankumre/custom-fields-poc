package in.clear.http.repository;

import in.clear.http.model.audit.SCAuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SCAuditEventRepository extends JpaRepository<SCAuditEvent, Long> {
    List<SCAuditEvent> findByEntityId(String entityId);
}
