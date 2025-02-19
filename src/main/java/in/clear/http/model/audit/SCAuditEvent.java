package in.clear.http.model.audit;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sc_audit_event")

public class SCAuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_timestamp")
    private String eventTimestamp;

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "event_type_id")
    private Long eventTypeId;

    @Column(name = "actor")
    private String actor;

    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
    @Column(columnDefinition = "jsonb", name = "current_data")
    private JsonNode currentData;

    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
    @Column(columnDefinition = "jsonb", name = "previous_data")
    private JsonNode previousData;

    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
    @Column(columnDefinition = "jsonb", name = "diff")
    private JsonNode diff;

    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
    @Column(columnDefinition = "jsonb", name = "metadata")
    private JsonNode metadata;
}
