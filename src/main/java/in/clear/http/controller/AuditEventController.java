package in.clear.http.controller;

import in.clear.http.model.audit.AuditEventTemplates;
import in.clear.http.model.audit.AuditEventType;
import in.clear.http.service.SCAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditEventController {
    private final SCAuditService scAuditService;

    @PostMapping("/event-type")
    public ResponseEntity<AuditEventType> createAuditEventType(@RequestBody AuditEventType auditEventType) {
        return ResponseEntity.ok(scAuditService.saveAuditEventType(auditEventType));
    }

    @GetMapping("/event-type/{id}")
    public ResponseEntity<AuditEventType> getAuditEventType(@RequestParam Long id) {
        return ResponseEntity.ok(scAuditService.getAuditEventTypeById(id));
    }

    @GetMapping("/event-type")
    public ResponseEntity<List<AuditEventType>> getAuditEventType() {
        return ResponseEntity.ok(scAuditService.listAuditEventType());
    }

    @PostMapping("/event-template")
    public ResponseEntity<AuditEventTemplates> createAuditEventTemplate(@RequestBody AuditEventTemplates auditEventTemplates) {
        return ResponseEntity.ok(scAuditService.saveAuditEventTemplates(auditEventTemplates));
    }

    @GetMapping("/event-template/{id}")
    public ResponseEntity<AuditEventTemplates> getAuditEventTemplate(@RequestParam Long id) {
        return ResponseEntity.ok(scAuditService.getAuditEventTemplatesById(id));
    }

    @GetMapping("/event-template")
    public ResponseEntity<List<AuditEventTemplates>> getAuditEventTemplate() {
        return ResponseEntity.ok(scAuditService.listAuditEventTemplates());
    }

    @GetMapping("/timeline-view")
    public ResponseEntity<List<String>> getTimelineView(@RequestParam String entityId) {
        return ResponseEntity.ok(scAuditService.getTimelineView(entityId));
    }
}
