package in.clear.http.repository;

import in.clear.http.model.audit.AuditEventTemplates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuditEventTemplatesRepository extends JpaRepository<AuditEventTemplates, Long> {
    Optional<AuditEventTemplates> findByEventTypeId(Long eventTypeId);
}
