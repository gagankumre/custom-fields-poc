package in.clear.http.repository.auditSdk;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@RequiredArgsConstructor
public class CustomRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public <T> T fetchOriginalEntityFromDb(Class<T> entityClass, Long entityId) {
        if (entityId == null) {
            return null;
        }

        T entity = entityManager.find(entityClass, entityId);
        if (entity != null) {
            entityManager.detach(entity);
        }

        return entityManager.find(entityClass, entityId);
    }
}
