package in.clear.http.dto.audit;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SCAuditEventRequest {

    @Builder.Default
    private LocalDateTime eventTimestamp = LocalDateTime.now();

    @Builder.Default
    private String idempotencyKey = UUID.randomUUID().toString();

    private String entityId;

    private Long eventTypeId;

    private String actor;

    private JsonNode currentData;

    private JsonNode previousData;

    private JsonNode diff;

    private JsonNode metadata;

}
