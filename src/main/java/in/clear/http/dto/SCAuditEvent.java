package in.clear.http.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SCAuditEvent<T> {

    @Builder.Default
    private LocalDateTime eventTimestamp = LocalDateTime.now();

    @Builder.Default
    private String identifier = UUID.randomUUID().toString();

    private T auditData;

    private Object metadata;

}
