package in.clear.http.aspect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.clear.http.dto.VendorAuditDTO;
import in.clear.http.dto.audit.SCAuditEventRequest;
import in.clear.http.mapper.VendorMapper;
import in.clear.http.model.Vendor;
import in.clear.http.repository.auditSdk.CustomRepository;
import in.clear.http.service.SCAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class VendorAuditAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final CustomRepository customRepository;
    private final SCAuditService scAuditService;

    @Around("execution(* in.clear.http.repository.VendorRepository.save(..)) && args(entity)")
    public Object saveVendorAspect(ProceedingJoinPoint joinPoint, Object entity) throws Throwable {
        try {
            log.info("Aspect on saveVendorAspect was called {}", joinPoint.getSignature().getName());
            Long entityId = getTrackedEntityId(entity);

            Vendor originalVendor = customRepository.fetchOriginalEntityFromDb(Vendor.class, entityId);
            JsonNode oldState = cloneObjectToJsonNode(creteVendorAuditDTO(originalVendor));

            Object result = joinPoint.proceed();

            var savedVendor = (Vendor) result;

            JsonNode newState = cloneObjectToJsonNode(creteVendorAuditDTO((Vendor) entity));

            sendAuditLogToMicroservice(savedVendor.getId(), oldState, newState);

            return result;
        } catch (Exception e) {
            log.error("Error in saveVendorAspect with message {}", e.getMessage(), e);
        }
        return joinPoint.proceed();
    }

    private VendorAuditDTO creteVendorAuditDTO(Vendor vendor) {
        return VendorMapper.INSTANCE.toVendorAuditDTO(vendor);
    }

    private JsonNode cloneObjectToJsonNode(Object object) {
        return (object != null) ? objectMapper.valueToTree(object) : null;
    }

    private Long getTrackedEntityId(Object entity) {
        if (entity instanceof Vendor) {
            return ((Vendor) entity).getId();
        } else {
            throw new IllegalArgumentException("Entity must implement TrackableEntity for auditing.");
        }
    }

    private void sendAuditLogToMicroservice(Long entityId, JsonNode oldState, JsonNode newState) {
        SCAuditEventRequest scAuditEventRequest = SCAuditEventRequest.builder()
                .entityId(entityId.toString())
                .currentData(newState)
                .previousData(oldState)
                .actor("GD")
                .eventTypeId(1L)
                .build();
        scAuditService.logAuditEvent(scAuditEventRequest);
    }
}
