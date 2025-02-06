package in.clear.http.aspect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import in.clear.http.model.Vendor;
import in.clear.http.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class VendorAspect {

    @PersistenceContext
    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;
    private final VendorRepository vendorRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* in.clear.http.repository.VendorRepository.save(..)) && args(entity)")
    public Object saveVendorAspect(ProceedingJoinPoint joinPoint, Object entity) throws Throwable {
        try {
            log.info("Aspect on saveVendorAspect was called {}", joinPoint.getSignature().getName());
            Long entityId = getTrackedEntityId(entity);

            Vendor originalVendor = fetchOriginalVendorFromDb(entityId);
            JsonNode oldState = (originalVendor != null) ? objectMapper.valueToTree(originalVendor) : null;

            Object result = joinPoint.proceed();
            JsonNode newState = objectMapper.valueToTree(entity);
            JsonNode diff = JsonDiff.asJson(oldState, newState);
            sendAuditLogToMicroservice(entityId, oldState, newState, diff);
            return result;
        } catch (Exception e) {
            log.error("Error in saveVendorAspect with message {}", e.getMessage(), e);
        }
        return joinPoint.proceed();
    }

    private Vendor fetchOriginalVendorFromDb(Long vendorId) {
        if (vendorId == null) {
            return null;
        }
        return transactionTemplate.execute(status -> {
            entityManager.clear(); // Clear the persistence context
            return entityManager.find(Vendor.class, vendorId);
        });
    }

    private JsonNode cloneEntityToJsonNode(Object entity) {
        return objectMapper.valueToTree(entity); // Converts entity to immutable JsonNode
    }

    private Long getTrackedEntityId(Object entity) {
        if (entity instanceof Vendor) {
            return ((Vendor) entity).getId();
        } else {
            throw new IllegalArgumentException("Entity must implement TrackableEntity for auditing.");
        }
    }

    private void sendAuditLogToMicroservice(Long entityId, JsonNode oldState, JsonNode newState, JsonNode diff) {
        System.out.printf("Audit Log for Entity ID: %s%n", entityId);
        System.out.printf("Old State: %s%n", oldState.toPrettyString());
        System.out.printf("New State: %s%n", newState.toPrettyString());
        System.out.printf("Diff: %s%n", diff.toPrettyString());
    }
}
